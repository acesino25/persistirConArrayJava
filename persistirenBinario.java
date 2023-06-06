Si deseas guardar más de un ArrayList en el mismo archivo binario, puedes utilizar un enfoque similar a la serialización mencionada anteriormente. Puedes crear una clase contenedora que contenga todos los ArrayList que deseas guardar y luego serializar esa clase. Aquí tienes un ejemplo:

java
Copy code
import java.io.*;

public class PersistArrayLists {

    public static void main(String[] args) {
        // Crear los ArrayList
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        ArrayList<String> list2 = new ArrayList<>();
        list2.add("Hola");
        list2.add("Mundo");

        // Crear la clase contenedora
        ArrayListContainer container = new ArrayListContainer();
        container.addList(list1);
        container.addList(list2);

        // Escribir la clase contenedora en un archivo binario
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("arrayLists.bin"));
            outputStream.writeObject(container);
            outputStream.close();
            System.out.println("ArrayLists persistidos en 'arrayLists.bin'");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Leer la clase contenedora desde el archivo binario
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("arrayLists.bin"));
            ArrayListContainer persistedContainer = (ArrayListContainer) inputStream.readObject();
            inputStream.close();
            System.out.println("ArrayLists leídos desde 'arrayLists.bin':");

            ArrayList<Integer> persistedList1 = persistedContainer.getList1();
            System.out.println("List1:");
            for (Integer item : persistedList1) {
                System.out.println(item);
            }

            ArrayList<String> persistedList2 = persistedContainer.getList2();
            System.out.println("List2:");
            for (String item : persistedList2) {
                System.out.println(item);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class ArrayListContainer implements Serializable {
    private ArrayList<Integer> list1;
    private ArrayList<String> list2;

    public ArrayListContainer() {
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
    }

    public void addList(ArrayList<Integer> list) {
        list1.addAll(list);
    }

    public void addList(ArrayList<String> list) {
        list2.addAll(list);
    }

    public ArrayList<Integer> getList1() {
        return list1;
    }

    public ArrayList<String> getList2() {
        return list2;
    }
}
En este ejemplo, se crea una clase ArrayListContainer que contiene dos ArrayList: list1 y list2. Puedes agregar más listas o modificar la clase según tus necesidades. Luego, se serializa la instancia de ArrayListContainer y se guarda en un archivo binario. Al leer el archivo binario, se obtiene la instancia de ArrayListContainer persistida y se pueden acceder a los ArrayList dentro de ella.

Recuerda que al utilizar la serialización, las clases que deseas persistir deben implementar la interfaz Serializable.