package io.github.ntask_api.web.rest.vm;

import lombok.Data;

/**
 * View Model object for storing the user's key and password.
 */
@Data
public class KeyAndPasswordVM {

    private String key;
    private String newPassword;
//    private String currentPassword;

}
