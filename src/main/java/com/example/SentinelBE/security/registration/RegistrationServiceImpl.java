package com.example.SentinelBE.security.registration;


import com.example.SentinelBE.authentication.email.EmailService;
import com.example.SentinelBE.authentication.exception.DuplicateUserException;
import com.example.SentinelBE.model.Role;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.repository.UserRepository;
import com.example.SentinelBE.security.token.TokenService;
import com.example.SentinelBE.security.token.UserToken;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;


    @Override
    @Transactional
    public void addUser(User user) {
        try {
            byte[] profilePic = new byte[0];
            try {
                profilePic = Files.readAllBytes(Paths.get("src/main/resources/CORRUPTDRONEMESSAGEICON.webp"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            var newUser = this.userRepository.save(User.builder()
                    .username(user.getUsername())
                    .hashedPassword(passwordEncoder.encode(user.getHashedPassword()))
                    .email(user.getEmail())
                    .role(Role.BASIC)
                    .profilePicture(profilePic)
                    .build());
            this.userRepository.save(newUser);
            var token = this.tokenService.createToken(newUser, 60);

            String htmlContent = getHtmlContent(token);
            emailService.sendHtmlEmail(newUser.getEmail(), "Registration", htmlContent);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateUserException("Username or email already exists.");
        }
         catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }



    @Override
    @Transactional
    public boolean enableUser(String token) {
        var retrievedToken = this.tokenService.getToken(token);
        if (this.tokenService.isTokenValid(retrievedToken)) {
            var user = this.userRepository.findById(retrievedToken.getUser().getId()).orElseThrow(() -> new EntityNotFoundException("No user associated with token."));
            user.setEnabled(true);
            userRepository.save(user);
            this.tokenService.removeToken(retrievedToken);
            return true;
        } else {
            throw new CredentialsExpiredException("Token is expired.");
        }
    }

    @Override
    public void removeUser(User user) {
        this.userRepository.delete(user);
    }

    @Transactional
    @Override
    public boolean updateUser(User user) {
        this.userRepository.findByUsername(user.getUsername()).ifPresentOrElse(fetchedUser -> {

                },
                () -> {
                    throw new EntityNotFoundException("User not found");
                });

        return true;
    }


    private  String getHtmlContent(UserToken token) {
        String validationToken = token.getToken();
        String htmlTemplate = """
                <!DOCTYPE html>
                <html xmlns:v="urn:schemas-microsoft-com:vml" xmlns:o="urn:schemas-microsoft-com:office:office" lang="en">
                                
                <head>
                	<title></title>
                	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                	<meta name="viewport" content="width=device-width, initial-scale=1.0"><!--[if mso]>
                <xml><w:WordDocument xmlns:w="urn:schemas-microsoft-com:office:word"><w:DontUseAdvancedTypographyReadingMail/></w:WordDocument>
                <o:OfficeDocumentSettings><o:PixelsPerInch>96</o:PixelsPerInch><o:AllowPNG/></o:OfficeDocumentSettings></xml>
                <![endif]--><!--[if !mso]><!--><!--<![endif]-->
                	<style>
                		* {
                			box-sizing: border-box;
                		}
                                
                		body {
                			margin: 0;
                			padding: 0;
                		}
                                
                		a[x-apple-data-detectors] {
                			color: inherit !important;
                			text-decoration: inherit !important;
                		}
                                
                		#MessageViewBody a {
                			color: inherit;
                			text-decoration: none;
                		}
                                
                		p {
                			line-height: inherit
                		}
                                
                		.desktop_hide,
                		.desktop_hide table {
                			mso-hide: all;
                			display: none;
                			max-height: 0px;
                			overflow: hidden;
                		}
                                
                		.image_block img+div {
                			display: none;
                		}
                                
                		sup,
                		sub {
                			font-size: 75%;
                			line-height: 0;
                		}
                                
                		@media (max-width:520px) {
                			.mobile_hide {
                				display: none;
                			}
                                
                			.row-content {
                				width: 100% !important;
                			}
                                
                			.stack .column {
                				width: 100%;
                				display: block;
                			}
                                
                			.mobile_hide {
                				min-height: 0;
                				max-height: 0;
                				max-width: 0;
                				overflow: hidden;
                				font-size: 0px;
                			}
                                
                			.desktop_hide,
                			.desktop_hide table {
                				display: table !important;
                				max-height: none !important;
                			}
                		}
                	</style><!--[if mso ]><style>sup, sub { font-size: 100% !important; } sup { mso-text-raise:10% } sub { mso-text-raise:-10% }</style> <![endif]-->
                </head>
                                
                <body class="body" style="background-color: #FFFFFF; margin: 0; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;">
                	<table class="nl-container" width="100%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #FFFFFF;">
                		<tbody>
                			<tr>
                				<td>
                					<table class="row row-1" align="center" width="100%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff;">
                						<tbody>
                							<tr>
                								<td>
                									<table class="row-content stack" align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; background-color: #ffffff; width: 500px; margin: 0 auto;" width="500">
                										<tbody>
                											<tr>
                												<td class="column column-1" width="100%" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top;">
                													<table class="image_block block-1" width="100%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;">
                														<tr>
                															<td class="pad" style="width:100%;">
                																<div class="alignment" align="center">
                																	<div style="max-width: 500px;"><img src="https://d15k2d11r6t6rl.cloudfront.net/pub/bfra/ri7fwuby/rd4/uso/ugg/name.png" style="display: block; height: auto; border: 0; width: 100%;" width="500" alt title height="auto"></div>
                																</div>
                															</td>
                														</tr>
                													</table>
                												</td>
                											</tr>
                										</tbody>
                									</table>
                								</td>
                							</tr>
                						</tbody>
                					</table>
                					<table class="row row-2" align="center" width="100%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;">
                						<tbody>
                							<tr>
                								<td>
                									<table class="row-content stack" align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 500px; margin: 0 auto;" width="500">
                										<tbody>
                											<tr>
                												<td class="column column-1" width="100%" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top;">
                													<table class="heading_block block-1" width="100%" border="0" cellpadding="10" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;">
                														<tr>
                															<td class="pad">
                																<h1 style="margin: 0; color: #000000; direction: ltr; font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif; font-size: 38px; font-weight: 700; letter-spacing: normal; line-height: 1.2; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 46px;"><span class="tinyMce-placeholder" style="word-break: break-word;">Welcome to SetinelExec</span></h1>
                															</td>
                														</tr>
                													</table>
                												</td>
                											</tr>
                										</tbody>
                									</table>
                								</td>
                							</tr>
                						</tbody>
                					</table>
                					<table class="row row-3" align="center" width="100%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;">
                						<tbody>
                							<tr>
                								<td>
                									<table class="row-content stack" align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-radius: 0; color: #000000; width: 500px; margin: 0 auto;" width="500">
                										<tbody>
                											<tr>
                												<td class="column column-1" width="100%" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top;">
                													<table class="paragraph_block block-1" width="100%" border="0" cellpadding="10" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;">
                														<tr>
                															<td class="pad">
                																<div style="color:#101112;direction:ltr;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:16px;font-weight:400;letter-spacing:0px;line-height:1.2;text-align:left;mso-line-height-alt:19px;">
                																	<p style="margin: 0;">Thank you for signing up. We're excited to have you on board. Please copy the token below to activate your account:</p>
                																</div>
                															</td>
                														</tr>
                													</table>
                												</td>
                											</tr>
                										</tbody>
                									</table>
                								</td>
                							</tr>
                						</tbody>
                					</table>
                					<table class="row row-4" align="center" width="100%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;">
                						<tbody>
                							<tr>
                								<td>
                									<table class="row-content stack" align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-radius: 0; color: #000000; width: 500px; margin: 0 auto;" width="500">
                										<tbody>
                											<tr>
                												<td class="column column-1" width="100%" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top;">
                													<table class="heading_block block-1" width="100%" border="0" cellpadding="10" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;">
                														<tr>
                															<td class="pad">
                																<h1 style="margin: 0; color: #000000; direction: ltr; font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif; font-size: 20px; font-weight: 700; letter-spacing: normal; line-height: 1.2; text-align: left; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 24px;"><span class="tinyMce-placeholder" style="word-break: break-word;">{{TOKEN}}</span></h1>
                															</td>
                														</tr>
                													</table>
                												</td>
                											</tr>
                										</tbody>
                									</table>
                								</td>
                							</tr>
                						</tbody>
                					</table>
                					<table class="row row-5" align="center" width="100%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;">
                						<tbody>
                							<tr>
                								<td>
                									<table class="row-content stack" align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-radius: 0; color: #000000; width: 500px; margin: 0 auto;" width="500">
                										<tbody>
                											<tr>
                												<td class="column column-1" width="100%" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top;">
                													<table class="heading_block block-1" width="100%" border="0" cellpadding="10" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;">
                														<tr>
                															<td class="pad">
                																<h1 style="margin: 0; color: #000000; direction: ltr; font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif; font-size: 16px; font-weight: 400; letter-spacing: normal; line-height: 1.2; text-align: left; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 19px;"><span class="tinyMce-placeholder" style="word-break: break-word;">The token will be available for 60 minutes.</span></h1>
                															</td>
                														</tr>
                													</table>
                												</td>
                											</tr>
                										</tbody>
                									</table>
                								</td>
                							</tr>
                						</tbody>
                					</table>
                					<table class="row row-6" align="center" width="100%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; background-size: auto;">
                						<tbody>
                							<tr>
                								<td>
                									<table class="row-content stack" align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-size: auto; color: #000000; background-color: #ffffff; border-radius: 0; width: 500px; margin: 0 auto;" width="500">
                										<tbody>
                											<tr>
                												<td class="column column-1" width="66.66666666666667%" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top;">
                													<table class="paragraph_block block-1" width="100%" border="0" cellpadding="10" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;">
                														<tr>
                															<td class="pad">
                																<div style="color:#000000;direction:ltr;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:19px;font-weight:700;letter-spacing:0px;line-height:1.2;text-align:left;mso-line-height-alt:23px;">
                																	<p style="margin: 0; margin-bottom: 3px;">Best regards,</p>
                																	<p style="margin: 0;">SentinelExec</p>
                																</div>
                															</td>
                														</tr>
                													</table>
                												</td>
                												<td class="column column-2" width="33.333333333333336%" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top;">
                													<table class="empty_block block-1" width="100%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;">
                														<tr>
                															<td class="pad">
                																<div></div>
                															</td>
                														</tr>
                													</table>
                												</td>
                											</tr>
                										</tbody>
                									</table>
                								</td>
                							</tr>
                						</tbody>
                					</table>
                				</td>
                			</tr>
                		</tbody>
                	</table><!-- End -->
                </body>
                                
                </html>
                                
                """;
        return htmlTemplate.replace("{{TOKEN}}", validationToken);

    }
}
