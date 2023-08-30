package com.mateusmsc.essential.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {
    private String title;
    private int status;
    private String detais;
    private String developerMessage;
    private LocalDateTime timeStamp;
}
