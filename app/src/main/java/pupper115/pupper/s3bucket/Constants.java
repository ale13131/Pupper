package pupper115.pupper.s3bucket;

/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

/**
 * Taken from the AWS GitHub for use in accessing the S3 bucket where our dog pictures are stored
 * on our server. These are for use for that bucket only
 */

public class Constants {

    public static final String COGNITO_POOL_ID = "us-east-1:800256ad-1679-4750-b2c5-15921ecb246b";

    /*
     * Region of your Cognito identity pool ID.
     */
    public static final String COGNITO_POOL_REGION = "us-east-1";

    /*
     * Note, you must first create a bucket using the S3 console before running
     * the sample (https://console.aws.amazon.com/s3/). After creating a bucket,
     * put it's name in the field below.
     */
    public static final String BUCKET_NAME = "pupper-user-info";

    /*
     * Region of your bucket.
     */
    public static final String BUCKET_REGION = "us-east-1";
}