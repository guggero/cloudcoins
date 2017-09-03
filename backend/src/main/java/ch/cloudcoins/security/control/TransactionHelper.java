package ch.cloudcoins.security.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

public class TransactionHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHelper.class);

    public static UserTransaction getUserTransaction() {
        try {
            InitialContext ctx = new InitialContext();
            return (UserTransaction) ctx.lookup("java:comp/UserTransaction");
        } catch (Exception e) {
            LOGGER.error("Could not get user transaction!", e);
        }
        return null;
    }

    public static UserTransaction startTransaction() {
        UserTransaction userTransaction = getUserTransaction();
        if (userTransaction != null) {
            try {
                userTransaction.begin();
                return userTransaction;
            } catch (Exception e) {
                LOGGER.error("Could not begin user transaction!", e);
            }
        }
        return null;
    }

    public static void commitTransaction(UserTransaction userTransaction) {
        try {
            userTransaction.commit();
        } catch (Exception e) {
            LOGGER.error("Could not commit user transaction! Trying rollback.", e);
            try {
                userTransaction.rollback();
            } catch (Exception e2) {
                // ignore
            }
        }
    }
}
