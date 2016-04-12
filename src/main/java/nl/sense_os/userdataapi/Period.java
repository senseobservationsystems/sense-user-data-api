package nl.sense_os.userdataapi;

/**
 * Created by tatsuya on 12/04/16.
 */
public enum Period {

    DAY {
        public String toString(){
            return "day";
        }
    },
    WEEK {
        public String toString(){
            return "week";
        }
    },
    MONTH {
        public String toString(){
            return "month";
        }
    }
}
