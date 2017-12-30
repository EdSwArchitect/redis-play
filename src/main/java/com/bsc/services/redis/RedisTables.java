package com.bsc.services.redis;

public interface RedisTables {
    public enum LOOKUP_TBL  { RESERVED(0), TBL_1(1), TBL_2(2), TBL_3(3);
        private final int db;

        LOOKUP_TBL(int db) {
            this.db = db;
        }

        private int db() {
            return this.db;
        }
    };
}
