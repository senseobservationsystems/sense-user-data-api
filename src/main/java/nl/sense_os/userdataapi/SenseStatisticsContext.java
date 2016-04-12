package nl.sense_os.userdataapi;

/**
 * Created by tatsuya on 22/03/16.
 */
public enum SenseStatisticsContext {
    DOMAIN {
        public String toString() {
            return "domain";
        }
    },

    GROUP {
        public String toString() {
            return "group";
        }
    },

    USER {
        public String toString() {
            return "user";
        }
    }
}
