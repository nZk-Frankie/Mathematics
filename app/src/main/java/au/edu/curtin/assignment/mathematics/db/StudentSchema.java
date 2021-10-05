package au.edu.curtin.assignment.mathematics.db;

public class StudentSchema {
    public static class StudentTable
    {
        public static final String DBNAME = "student";
        public static class Cols
        {
            public static final String ID = "id";
            public static final String FIRST_NAME = "name";
            public static final String LAST_NAME = "email";
            public static final String IMAGE = "IMAGE";
        }
    }
}
