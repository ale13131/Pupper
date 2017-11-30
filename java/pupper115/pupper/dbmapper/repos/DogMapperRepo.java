package pupper115.pupper.dbmapper.repos;

/**
 * Created by aaron on 11/1/2017.
 */

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import pupper115.pupper.dbmapper.tables.TblDog;

public class DogMapperRepo {
    private DynamoDBMapper dbMapper;

    public DogMapperRepo(AmazonDynamoDB amazondynamoDB){
        dbMapper = new DynamoDBMapper(amazondynamoDB);
    }

    public void insert(TblDog dog){
        dbMapper.save(dog);
    }

    public TblDog getDog(String dogname){
        TblDog dog = dbMapper.load(TblDog.class,dogname);

        return dog;
    }
}
