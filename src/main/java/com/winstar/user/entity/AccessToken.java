package com.winstar.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * token
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CBC_USER_ACCESS_TOKEN")
public class AccessToken {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 账号ID
     */
    private String accountId;
    /**
     * 账号tokenID
     */
    private String tokenId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date updateTime;


    public static void main(String[] args) {
        String s = "JENKINS_NODE_COOKIE=dontKillMe" +
                "APP_GREP=winstar-cbc-platform-api\n" +
                "  cd ./target\n" +
                "    n=`ps -ef|grep $APP_GREP|grep -v grep|wc -l`\n" +
                "    if [ 0 -ne $n ];then\n" +
                "        kill -9 `ps -ef | grep $APP_GREP|grep -v grep| awk  '{print $2}'`\n" +
                "    fi\n" +
                "  if [ -f *.jar ];then\n" +
                "    APP=$(ls *.jar)\n" +
                "  else\n" +
                "    echo 'APP NOT FOUND!'\n" +
                "    exit 1\n" +
                "  fi\n" +
                "    nohup java -jar $APP > ./cbc.log &\n" +
                "    sleep 2\n" +
                "    echo -e \"starting  ............... [ ok ]\" ";

        System.out.println(s);
    }
}
