package com.ricoandilet.springtools.iam;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.ChangePasswordRequest;
import com.amazonaws.services.identitymanagement.model.CreateLoginProfileRequest;
import com.amazonaws.services.identitymanagement.model.DeleteLoginProfileRequest;
import org.junit.jupiter.api.Test;

public class AWSIAM {

   private final String accessKey = "accessKey";
   private final String secretKey = "secretKey";

   private final AmazonIdentityManagement client = AmazonIdentityManagementClientBuilder.standard()
           .withRegion(Regions.US_WEST_1)
           .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
           .build();

   @Test
   void updatePwd(){
      ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
      changePasswordRequest.withNewPassword("password");

      DeleteLoginProfileRequest deleteLoginProfileRequest = new DeleteLoginProfileRequest("username");
      client.deleteLoginProfile(deleteLoginProfileRequest);

      CreateLoginProfileRequest createLoginProfileRequest = new CreateLoginProfileRequest();
      createLoginProfileRequest.withUserName("username");
      createLoginProfileRequest.withPassword("password");
      client.createLoginProfile(createLoginProfileRequest);

   }
}
