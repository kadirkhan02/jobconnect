package com.jobportal.jobconnect.response;

import com.jobportal.jobconnect.dto.response.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ApiResponse<T> {

    private String message;
    private int status;
    private String timestamp;
    private T data;
    private boolean success;
    

    private ApiResponse(int status, String message)
    {
        this.timestamp= LocalDateTime.now().toString();
    }
    public  static<T> ApiResponse<T> success(T data,String message)
    {
        ApiResponse<T>res = new ApiResponse<>();
        res.success=true;
        res.message=message;
        res.data=data;
        res.status=201;
        return res;

    }
    public static<T> ApiResponse<T> created(T data, String message)
    {
        ApiResponse<T>res= new ApiResponse<>();
        res.success=true;
        res.message=message;
        res.data=data;
        res.status=201;
        return res;
    }
    public  static<T> ApiResponse<T> error(String message,int status)
    {
        ApiResponse<T>res=new ApiResponse<>();
        res.success=false;
        res.message=message;
        res.status=status;
        res.data=null;

        return res;
    }


}
