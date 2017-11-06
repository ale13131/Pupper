package pupper115.pupper.dbmapper.repos;

/**
 * Created by aaron on 11/1/2017.
 */

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import pupper115.pupper.dbmapper.tables.TblUser;

public class UserMapperRepo {
    private DynamoDBMapper dbMapper;

    public UserMapperRepo(AmazonDynamoDB amazondynamoDB){
        dbMapper = new DynamoDBMapper(amazondynamoDB);
    }

    public void insert(TblUser user){
        dbMapper.save(user);
    }

    public TblUser getUser(String username){
        TblUser user = dbMapper.load(TblUser.class,username);

        return user;
    }
}
