package nl.sense_os.userdataapi;

/**
 * Created by tatsuya on 12/04/16.
 */
public enum AggregationType {

    SUM {
        public String toString(){
            return "sum";
        }
    },

    AVERAGE {
        public String toString(){
            return "average";
        }
    },

    DISTRIBUTION {
        public String toString(){
            return "distribution";
        }
    }
}
