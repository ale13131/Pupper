package pupper115.pupper.dbmapper.tables;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * This is the access functions for the dog table. We have extra functions for the next iteration
 * of the app already programmed in, we just need to use them for future updates
 */

@DynamoDBTable(tableName = "pupper-mobilehub-909033989-tbl_Dog")

public class TblDog {
    private String _userId;
    private String _ownerId;
    private Boolean _atPound;
    private Double _dogAge;
    private String _dogBio;
    private String _dogBreed;
    private String _dogLocation;
    private String _dogName;
    private Boolean _isOwned;
    private Double _numOfLikes;
    private String _comments;
    private String _likedBy;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "ownerId")
    @DynamoDBAttribute(attributeName = "ownerId")
    public String getOwnerId() {
        return _ownerId;
    }

    public void setOwnerId(final String _ownerId) {
        this._ownerId = _ownerId;
    }
    @DynamoDBAttribute(attributeName = "atPound")
    public Boolean getAtPound() {
        return _atPound;
    }

    public void setAtPound(final Boolean _atPound) {
        this._atPound = _atPound;
    }
    @DynamoDBAttribute(attributeName = "dogAge")
    public Double getDogAge() {
        return _dogAge;
    }

    public void setDogAge(final Double _dogAge) {
        this._dogAge = _dogAge;
    }
    @DynamoDBAttribute(attributeName = "dogBio")
    public String getDogBio() {
        return _dogBio;
    }

    public void setDogBio(final String _dogBio) {
        this._dogBio = _dogBio;
    }
    @DynamoDBAttribute(attributeName = "dogBreed")
    public String getDogBreed() {
        return _dogBreed;
    }

    public void setDogBreed(final String _dogBreed) {
        this._dogBreed = _dogBreed;
    }
    @DynamoDBAttribute(attributeName = "dogLocation")
    public String getDogLocation() {
        return _dogLocation;
    }

    public void setDogLocation(final String _dogLocation) {
        this._dogLocation = _dogLocation;
    }
    @DynamoDBAttribute(attributeName = "dogName")
    public String getDogName() {
        return _dogName;
    }

    public void setDogName(final String _dogName) {
        this._dogName = _dogName;
    }
    @DynamoDBAttribute(attributeName = "isOwned")
    public Boolean getIsOwned() {
        return _isOwned;
    }

    public void setIsOwned(final Boolean _isOwned) {
        this._isOwned = _isOwned;
    }

    @DynamoDBAttribute(attributeName = "numOfLikes")
    public Double getLikes() {
        return _numOfLikes;
    }

    public void likeDog() { ++this._numOfLikes; }
    public void setLikes(final Double _numOfLikes) { this._numOfLikes = _numOfLikes; }

    @DynamoDBAttribute(attributeName = "comments")
    public String getComments() {
        return _comments;
    }

    public void setComments(final String _comments) {
        this._comments = this._comments + _comments ;
    }

    @DynamoDBAttribute(attributeName = "likedBy")
    public String getLikedBy() {
        return _likedBy;
    }

    public void setLikedBy(final String _userName) {
        this._likedBy = this._likedBy + " " + _userName ;
    }
}
