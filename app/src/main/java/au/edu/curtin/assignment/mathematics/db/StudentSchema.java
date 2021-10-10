package au.edu.curtin.assignment.mathematics.db;

public class StudentSchema {
    public static class StudentTable
    {
        public static final String DBNAME = "student";
        public static class Cols
        {
            public static final String ID = "id";
            public static final String FIRST_NAME = "firstname";
            public static final String LAST_NAME = "lastName";
            public static final String IMAGE = "IMAGE";
        }
    }
}
