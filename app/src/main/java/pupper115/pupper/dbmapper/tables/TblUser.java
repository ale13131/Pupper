package pupper115.pupper.dbmapper.tables;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Map;

/**
 * This is the access functions for the user table. We have extra functions for the next iteration
 * of the app already programmed in, we just need to use them for future updates
 */

@DynamoDBTable(tableName = "pupper-mobilehub-909033989-tbl_User")

public class TblUser {
    private String _userId;
    private Map<String, String> _dogIDs;
    private Boolean _isAdopting;
    private String _userEmail;
    private String _userFN;
    private String _userLN;
    private String _userPassword;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

    @DynamoDBAttribute(attributeName = "dogIDs")
    public Map<String, String> getDogIDs() {
        return _dogIDs;
    }

    public void setDogIDs(final Map<String, String> _dogIDs) {
        this._dogIDs = _dogIDs;
    }

    @DynamoDBAttribute(attributeName = "isAdopting")
    public Boolean getIsAdopting() {
        return _isAdopting;
    }

    public void setIsAdopting(final Boolean _isAdopting) {
        this._isAdopting = _isAdopting;
    }

    @DynamoDBAttribute(attributeName = "userEmail")
    public String getUserEmail() {
        return _userEmail;
    }

    public void setUserEmail(final String _userEmail) {
        this._userEmail = _userEmail;
    }

    @DynamoDBAttribute(attributeName = "userFN")
    public String getUserFN() {
        return _userFN;
    }

    public void setUserFN(final String _userFN) {
        this._userFN = _userFN;
    }

    @DynamoDBAttribute(attributeName = "userLN")
    public String getUserLN() {
        return _userLN;
    }

    public void setUserLN(final String _userLN) {
        this._userLN = _userLN;
    }

    @DynamoDBAttribute(attributeName = "userPassword")
    public String getUserPassword() {
        return _userPassword;
    }

    public void setUserPassword(final String _userPassword) {
        this._userPassword = _userPassword;
    }
}
