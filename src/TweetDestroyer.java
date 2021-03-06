import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class TweetDestroyer {

    private final String consumerKeyOAuth;
    private final String consumerSecretOAuth;
    private final String accessTokenOAuth;
    private final String accessSecretOAuth;

    private Twitter twitterAccount;
    private boolean built = false;

    public TweetDestroyer(String consumerKeyOAuth, String consumerSecretOAuth,
                          String accessTokenOAuth, String accessSecretOAuth) {
        this.consumerKeyOAuth = consumerKeyOAuth;
        this.consumerSecretOAuth = consumerSecretOAuth;
        this.accessTokenOAuth = accessTokenOAuth;
        this.accessSecretOAuth = accessSecretOAuth;
    }

    public void build() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKeyOAuth)
                .setOAuthConsumerSecret(consumerSecretOAuth)
                .setOAuthAccessToken(accessTokenOAuth)
                .setOAuthAccessTokenSecret(accessSecretOAuth);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitterAccount = tf.getInstance();
        built = true;
    }

    public int deleteTweets() throws TwitterException {
        int tweetsDeleted = 0;
        if (!built) {
            return 0;
        }
        Paging paging = new Paging();
        paging.count(1000);

        List<Status> statuses = twitterAccount.getUserTimeline(twitterAccount.getId(), paging);
        if (statuses != null) {
            for (Status status : statuses) {
                twitterAccount.destroyStatus(status.getId());
                tweetsDeleted++;
            }
        }
        return tweetsDeleted;
    }
}
