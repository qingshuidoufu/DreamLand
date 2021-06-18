package wang.dreamland.www.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Data
public class User {
    @Id//标识主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增长策略
    private Long id;

    private String email;

    private String password;

    private String phone;

    private String nickName;

    private String state;

    private String imgUrl;

    private String enable;


}