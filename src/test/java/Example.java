import com.vane.xrm.Xrm;

public class Example {
    @Xrm("번호")
    private int number;
    @Xrm("이름")
    private String name;
    @Xrm("성별")
    private String gender;

    public void printExample() {
        System.out.printf("번호: %d, 이름: '%s', 성별: '%s'%n", number, name, gender);
    }
}
