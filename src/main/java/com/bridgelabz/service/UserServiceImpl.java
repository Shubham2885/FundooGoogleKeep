/******************************************************************************
 *  Compilation:  javac -d bin ElasticSearchConfig.java
 *  Execution:    java -cp bin com.bridgelabz.config;
 *  						  
 *  
 *  Purpose:      ElasticSearch configuration class
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   11-12-2019
 *
 ******************************************************************************/
package com.bridgelabz.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//import com.amazonaws.services.s3.AmazonS3;
import com.bridgelabz.dto.LoginDTO;
import com.bridgelabz.dto.ResetPasswordDTO;
import com.bridgelabz.dto.UserDTO;
import com.bridgelabz.exception.custome.CustomException;
import com.bridgelabz.exception.custome.CustomException.BadCredintial;
import com.bridgelabz.model.RabbitMqModel;
import com.bridgelabz.model.RedisModel;
import com.bridgelabz.model.User;
import com.bridgelabz.reposiitory.IRedisRepository;
import com.bridgelabz.reposiitory.NoteRepository;
import com.bridgelabz.reposiitory.UserRepository;
import com.bridgelabz.response.Response;
import com.bridgelabz.utility.JwtUtil;
import com.bridgelabz.utility.MessageReference;
import com.bridgelabz.utility.ResponseMessages;
import com.bridgelabz.utility.SendEmail;
@Service
public class UserServiceImpl implements IUserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	private SendEmail sendEmail;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	NoteRepository noteRepository;
	@Autowired
	private IRedisRepository redisRepository;
	ModelMapper mapper = new ModelMapper();

	@Autowired
	private RabbitTemplate rabbitTemplate;
	//private AmazonS3 s3client;

    @Value("${endpointUrl}")
    private String endpointUrl;
    @Value("${bucketName}")
    private String bucketName;
    @Value("${accessKey}")
    private String accessKey;
    @Value("${secretKey}")
    private String secretKey;
	/**
	 * @purpose : return all users
	 * @return : return users list
	 */
    @Override
	public Response getAllUser() {
		return new Response(ResponseMessages.STATUS200, ResponseMessages.ALL_USERS, userRepository.findAll());
	}

	/**
	 * @purpose : add new user
	 * @param userDTO : store info of user
	 * @return : return ok if everything is ok
	 */
    @Override
	public Response addUser(UserDTO userDTO) {
		Optional<User> user1 = userRepository.findByEmail(userDTO.getEmail());
		if (!user1.isEmpty()) 
			return new Response(ResponseMessages.STATUS200, ResponseMessages.USER_EMAIL_EXIST, null);
		
		if (!userDTO.getPassword().equals(userDTO.getCheckPassword()))
			return new Response(ResponseMessages.STATUS200, ResponseMessages.PASSWORD_DOES_NOT_MATCH, null);
			
		User user = mapper.map(userDTO, User.class);
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		userRepository.save(user);
		RabbitMqModel model = new RabbitMqModel(MessageReference.VERIFY_MAIL_SUBJECT, user.getEmail(), MessageReference.VERIFY_MAIL_TEXT+jwtUtil.generateToken(user.getEmail()));
		rabbitTemplate.convertAndSend("myexchange", "shubham.#", model);
		//sendEmail.sendMail(jwtUtil.generateToken(user.getEmail()), user.getEmail(), MessageReference.VERIFY_MAIL_SUBJECT, MessageReference.VERIFY_MAIL_TEXT);
		return new Response(ResponseMessages.STATUS200, MessageReference.MAIL_SENDED, null);	
	}

	/**
	 * @purpose : checking the login activity if credential is true then return token
	 * @param loginDTO : credential info
	 * @return : return the token
	 * @throws BadCredintial : exception if user provide bad credential
	 */
    @Override
	public Response login(LoginDTO loginDTO) throws CustomException.BadCredintial {
		/*
		 * User user =
		 * userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(()->new
		 * UserNotFoundException("User Not Found..")); if
		 * (loginDTO.getPassword().equals(user.getPassword())) { String token =
		 * jwtUtil.generateToken(loginDTO.getEmail()); return new
		 * Response(Response.STATUS200, Response.LOGIN_SUCCESSFULL+" Token : "+token,
		 * null); } else { return new Response(Response.STATUS200,
		 * Response.LOGIN_UNSUCCESSFULL, null); }
		 */
		User user = userRepository.findByEmail(loginDTO.getEmail())
				.orElseThrow(() -> new CustomException.UserNotFoundException("User Not Found.."));
		if (user.isActive()) {
			if(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
			final String token = jwtUtil.generateToken(user.getEmail());
			RedisModel model = new RedisModel();
			//System.out.println(LocalDateTime.now().toString());
			model.setEmail(user.getEmail());model.setId(token);model.setLastLogin(LocalDateTime.now().toString());
			redisRepository.add(model);
			return new Response(ResponseMessages.STATUS200, ResponseMessages.LOGIN_SUCCESSFULL + " Token=" + token, null);
			}else 
				throw new CustomException.BadCredintial(ResponseMessages.LOGIN_UNSUCCESSFULL);
		} else 
			return new Response(ResponseMessages.STATUS200, ResponseMessages.VERIFY_ACCOUNT_FIRST, null);
	}

	/**
	 * @purpose : verify user name as email id and return reset password link
	 * @param username : store user name as email id
	 * @return : return reset password link through provide mail id
	 */
    @Override
	public Response forgetPassword(String username) {
		userRepository.findByEmail(username).orElseThrow(() -> new CustomException.UserNotFoundException("User Not Found.."));
		String token = jwtUtil.generateToken(username);
		sendEmail.sendMail(token, username, MessageReference.RESET_PASSWORD_MAIL_SUBJECT, MessageReference.RESET_PASSWORD_MAIL_TEXT);
			return new Response(ResponseMessages.STATUS200, MessageReference.RESET_PASSWORD_MAIL_SENDED_MESSAGE, null);
	}

	/**
	 * @purpose : Reset the password 
	 * @param resetPasswordDTO : store password credential
	 * @param token : current user token for extracting user name of user
	 * @return : return OK message
	 */
    @Override
	public Response resetPassword(ResetPasswordDTO resetPasswordDTO, String token) {
		if (resetPasswordDTO.getPassword().equals(resetPasswordDTO.getCheckPassword())) {
			User user = userRepository.findByEmail(jwtUtil.validateToken(token))
					.orElseThrow(() -> new CustomException.UserNotFoundException("User Not Found.."));
			user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
			userRepository.save(user);
			return new Response(ResponseMessages.STATUS200, ResponseMessages.PASSWORD_CHANGE, null);
		} else {
			return new Response(ResponseMessages.STATUS200, ResponseMessages.PASSWORD_DOES_NOT_MATCH, null);
		}
	}

	/**
	 * @purpose : if user is not active then send link for active account
	 * @param token : account verification token
	 * @return : return OK message
	 */
    @Override
	public Response accountVerification(String token) {
		User user = userRepository.findByEmail(jwtUtil.validateToken(token))
				.orElseThrow(() -> new CustomException.UserNotFoundException("User Not Found.."));
		user.setActive(true);
		userRepository.save(user);
		return new Response(ResponseMessages.STATUS200, ResponseMessages.ACCOUNT_ACTIVATED, null);
	}

	/**
	 * @purpose : return current user details 
	 * @param token : current user token
	 * @return : return user details
	 */
    @Override
	public Response getUserDetails(String token) {
    	RedisModel model = redisRepository.findUser(token);
		Optional<User> user = userRepository.findByEmail(jwtUtil.validateToken(model.getId()));
		List<Object> list = new ArrayList<>();
		list.add(user);
		list.add(model);
		return new Response(ResponseMessages.STATUS200, ResponseMessages.USER_DETAILS, list);
	}
/*	
	public Response uploadProfilePic(MultipartFile multipartFile,String token) {
		String fileUrl="";
		Optional<User> user = userRepository.findByEmail(jwtUtil.validateToken(token));
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl+"/"+bucketName+"/"+fileName;
			uploadFileToS3Bucket(fileName, file);
			file.delete();
			user.get().setProfilePic(fileUrl);
			userRepository.save(user.get());
		}catch (Exception e) {
			
		}
		return new Response(Response.STATUS200, "Pofile Pic Uploaded", null);
	}
	
	private void uploadFileToS3Bucket(String fileName,File file) {
		s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));
	}
	private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException{
		File convertedFile = new File(multipartFile.getOriginalFilename());
		System.out.println(multipartFile.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertedFile);
		fos.write(multipartFile.getBytes());
		fos.close();
		return convertedFile;
	}
	
	private String generateFileName(MultipartFile multipartFile) {
		return new Date().getTime()+"-"+multipartFile.getOriginalFilename();
	}
	
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		s3client= new AmazonS3Client(credentials);
	}
	
	public Response deleteFileFromS3Bucket(String token) {
		Optional<User> user = userRepository.findByEmail(jwtUtil.validateToken(token));
		String fileUrl=user.get().getProfilePic();
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName+"/",fileName));
		user.get().setProfilePic(null);
		userRepository.save(user.get());
		return new Response(Response.STATUS200, "Profile Pic Deleted", null);
	}
*/

	@Override
	public Response logout(String token) {
		if(redisRepository.delete(token)==1)
			return new Response(ResponseMessages.STATUS200, "Logout goto login", null);
		return new Response(ResponseMessages.STATUS200, "You Aleady Logout", null);
	}
}
