package base.project;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {
        String dto = "111,qeweq,324234";

        List<String> list = Arrays.stream(dto.split(",")).collect(Collectors.toList());
        List<String> list3 = Arrays.asList(dto.split(","));

        int[] arr1 = {1, 2, 3};
        List<Integer> list1 = Arrays.stream(arr1).boxed().collect(Collectors.toList());

        Integer[] arr2 = {1, 2, 3};
        List<Integer> list2 = Arrays.asList(arr2);

        Set<String> phoneNumbers = new HashSet<>();
        phoneNumbers.add("1");
        phoneNumbers.add("3");
        phoneNumbers.add("2");

        String[] strings = phoneNumbers.toArray(new String[0]);
        System.out.println(strings);


    }
}
