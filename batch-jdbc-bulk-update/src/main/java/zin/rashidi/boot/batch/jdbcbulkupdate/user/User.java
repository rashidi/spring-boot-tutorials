package zin.rashidi.boot.batch.jdbcbulkupdate.user;

/**
 * @author Rashidi Zin
 */
record User(Long id, String username, Status status) {

    enum Status {

        ACTIVE,
        INACTIVE,
        DORMANT,
        DELETED

    }

}
