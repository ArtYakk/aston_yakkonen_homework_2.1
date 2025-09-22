public class Test {
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMapImpl<>();
        map.put("Artem", 1);
        map.put("Vetal", 2);
        map.put("Grisha", 3);
        map.put("Gosha", 4);
        map.put("Vanya", 5);

        map.print();

        System.out.println("===========================================");

        System.out.println(map.get("Artem"));
        System.out.println(map.get("Vanya"));
        System.out.println(map.get("NotExist"));

        System.out.println("===========================================");

        map.remove("Artem");
        map.print();

        System.out.println("===========================================");
        System.out.println(map.remove("NotExist"));
    }
}
