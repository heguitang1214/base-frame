package base.project.date;

import java.util.Date;

public class DataTest {


    public static void main(String[] args) {

        Date date = new Date(2019, 12, 31, 11, 12, 13);
        System.out.println(date);

        Date date1 = new Date(2019 - 1900, 11, 31, 11, 12, 13);
        System.out.println(date1);

    }


}
