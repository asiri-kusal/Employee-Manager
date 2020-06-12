/**
 * The LoginRequest class use to map user authorization information to single object.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
   	private String username;

   	@NotBlank
   	private String password;

   	public String getUsername() {
   		return username;
   	}

   	public void setUsername(String username) {
   		this.username = username;
   	}

   	public String getPassword() {
   		return password;
   	}

   	public void setPassword(String password) {
   		this.password = password;
   	}

}