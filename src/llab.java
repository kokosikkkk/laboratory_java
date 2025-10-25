import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import java.io.FileReader;

class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Чтобы запустить To-do-List введите <Start>, иначе <Close>: ");
        String beginning = sc.nextLine();
        if (beginning.equalsIgnoreCase("Start")) {
            System.out.println("To-do-List открывается!");
            System.out.println("~~~~~~~~ To-Do List ~~~~~~~~");
            WorkingTasks workingTasks = new WorkingTasks();
            while (true) {
                System.out.println("Добро пожаловать в ваш список дел! Ознакомтесь с возможными действиями:");
                System.out.println("1. Создать задачу.");
                System.out.println("2. Редактировать задачу.");
                System.out.println("3. Удалить задачу.");
                System.out.println("4. Показать все задачи.");
                System.out.println("5. Cортировка.");
                System.out.println("6. Поиск. ");
                System.out.println("7. Выход. ");

                if (sc.hasNextInt()) {
                    int point = sc.nextInt();
                    sc.nextLine();

                    switch (point) {
                        case 1:
                            workingTasks.AddTask();
                            break;
                        case 2:
                            workingTasks.Aditing();
                            break;
                        case 3:
                            workingTasks.Delition();
                            break;
                        case 4:
                            workingTasks.Output();
                            break;
                        case 5:
                            workingTasks.Sort();
                            break;
                        case 6:
                            workingTasks.Search();
                            break;
                        case 7:
                            System.out.println("Выход.");
                            sc.close();
                            return;
                        default:
                            System.out.println("Такого действия не существует. Проверьте корректность ввода.");

                    }
                }

            }
        } else {
            if (beginning.equalsIgnoreCase("Close")) {
                System.out.println("До встречи!");
            } else {
                System.out.println("Вероятно вы ошиблись с вводом, проверьте корректно ли написаны слова.");
            }
            sc.close();
        }
    }
}
class DataWrapper {
    private List<String> uniqueNum;
    private List<String> heading;
    private List<String> description;
    private List<String> time;
    private List<String> priority;

    public DataWrapper(){
        this.uniqueNum = new ArrayList<>();
        this.heading = new ArrayList<>();
        this.description = new ArrayList<>();
        this.time = new ArrayList<>();
        this.priority = new ArrayList<>();

    }
    public List<String> getUniqueNum(){
        return uniqueNum;
    }

    public List<String> getHeading() {
        return heading;
    }
    public List<String> getDescription(){
        return description;
    }
    public List<String> getTime(){
        return time;
    }
    public List<String> getPriority(){
        return priority;
    }


    public void setHeading(int index, String heading) {
        if (heading == null || heading.isEmpty()){
            throw new IllegalArgumentException("Это поле обязательно для заполнения!");
        }
        if (heading.length() > 150){
            throw new IllegalArgumentException("Слишком много символов!");
        }
        while (this.heading.size() <= index){
            this.heading.add("");
        }
        this.heading.set(index, heading);
    }

    public void setDescription(int index, String description) {
        if (description == null || description.isEmpty()){
            throw new IllegalArgumentException("Это поле обязательно для заполнения!");
        }
        if (description.length() > 350){
            throw new IllegalArgumentException("Слишком много символов!");
        }
        while (this.description.size() <= index){
            this.description.add("");
        }
        this.description.set(index, description);

    }

    public void setTime(int index, String time) {
        if (time == null || time.isEmpty()){
            throw new IllegalArgumentException("Это поле обязательно для заполнения!");
        }
        if (!time.matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
            throw new IllegalArgumentException("Дата должна быть введена таким образом: ДД.ММ.ГГГГ");
        }
        String[] arr = time.split("\\.");
        int day = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int year = Integer.parseInt(arr[2]);
        if (day <= 0 || day > 31){
            throw new IllegalArgumentException("Ошибка: данной даты не существует.");
        }
        if (month < 1 || month > 12){
            throw new IllegalArgumentException("Ошибка: данной даты не существует.");
        }
        while (this.time.size() <= index) {
            this.time.add("");
        }
        this.time.set(index, time);
    }

    public void setPriority(int index, String priority) {
        if (priority == null || priority.isEmpty()){
            throw new IllegalArgumentException("Это поле обязательно для заполнения!");
        }
        List<String> variety = Arrays.asList("Высокий", "Средний", "Низкий");
        if (!variety.contains(priority)){
            throw new IllegalArgumentException("Неверный ввод");
        }
        while (this.priority.size() <= index){
            this.priority.add("");
        }
        this.priority.set(index, priority);
    }
    public void setHeading(List<String> heading) {
        this.heading = heading;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }
    public void setTime(List<String> time) {
        this.time = time;
    }

    public void setPriority(List<String> priority) {
        this.priority = priority;
    }
}//класс работы с задачами
class WorkingTasks{
    private static final String FILE_NAME = "to_do_list.json";
    private File file = new File(FILE_NAME);
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private DataWrapper dataWrapper;

    class Unification {
        String id;
        String heading;
        String description;
        String time;
        String priority;

        public Unification(String id, String heading, String description, String time, String priority) {
            this.id = id;
            this.heading = heading;
            this.description = description;
            this.time = time;
            this.priority = priority;
        }
    }
    public WorkingTasks() { //проверка на наличие файла и его создание
        if (!file.exists()) {
            System.out.println("Создание файла со списком ваших задач, чтобы избежать их потери.");
            dataWrapper = new DataWrapper();
            try (FileWriter writer = new FileWriter(FILE_NAME)) {
                gson.toJson(dataWrapper, writer);
                System.out.println("Файл успешно создан! Можете приступать к записи ваших задач!");
            } catch (IOException e) {
                System.out.println("Возникли ошибки при создании файла");
            }
        } else {
            try (FileReader reader = new FileReader(FILE_NAME)) {
                dataWrapper = gson.fromJson(reader, DataWrapper.class);
            } catch (IOException e) {
                System.out.println("Ошибка во время чтении файла.");
                dataWrapper = new DataWrapper();
            }
        }
    }
    public DataWrapper getDataWrapper() {
        return dataWrapper;
    }//добавление задачи
    public void AddTask(){
        Scanner scanner = new Scanner(System.in);
        String uniqueNum = UUID.randomUUID().toString();
        try {
            System.out.print("Введите заголовок: ");
            String heading = scanner.nextLine();
            dataWrapper.setHeading(dataWrapper.getHeading().size(), heading);
            System.out.print("Введите описание: ");
            String description = scanner.nextLine();
            dataWrapper.setDescription(dataWrapper.getDescription().size(), description);
            System.out.print("Введите срок (ДД.ММ.ГГГГ): ");
            String time = scanner.nextLine();
            dataWrapper.setTime(dataWrapper.getTime().size(), time);
            System.out.print("Введите приоритет задачи (Высокий/Средний/Низкий): ");
            String priority = scanner.nextLine();
            dataWrapper.setPriority(dataWrapper.getPriority().size(), priority);
            dataWrapper.getUniqueNum().add(uniqueNum);


            try (FileWriter writer = new FileWriter(FILE_NAME)) {
                gson.toJson(dataWrapper, writer);
                System.out.println("Задача добавлена.");
            } catch (IOException e) {
                System.out.println("Ошибка сохранения! Проверьте ввели ли вы данные верно");
            }
        }catch (IllegalArgumentException e){
            System.out.println("Ошибка!" + e.getMessage());
            return;
        }
    }//редактирование задачи по критерию (уникальному индификатору);
    public void Aditing() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите уникальный номер задачи, чтобы ее редактировать: ");
        String criteria = scanner.nextLine();
        boolean flag = false;
        List<String> ID = dataWrapper.getUniqueNum();
        int j = -1;
        for (int i = 0; i < ID.size(); i++) {
            if (ID.get(i).equals(criteria)) {
                j = i;
                flag = true;
                break;
            }
        }
        if (!flag) {
            System.out.println("К сожалению ваша задача не найдена, проверьте правильно ли введи данные");
            return;
        }
        System.out.println("Для того, чтобы редактировать задачу, выберети элемент, который хотите изменить.");
        System.out.println("Чтобы изменить заголовок, введите <Заголовок" + "\n"
                + "Чтобы изменить описание введите <Описание>" + "\n"
                + "Чтобы изменить дату введите <Дата>" + "\n" + "Чтобы изменить приоритет введите <Приоритет>");
        System.out.print("Введите параметр: ");
        String wish = scanner.nextLine();
        switch (wish) {
            case "Заголовок":
                System.out.print("Введите заголовок: ");
                String newHeading = scanner.nextLine();
                try{
                    dataWrapper.setHeading(j, newHeading);
                    System.out.println("Ваши изменения:");
                    Updates(dataWrapper.getUniqueNum().get(j), newHeading,
                            dataWrapper.getDescription().get(j), dataWrapper.getTime().get(j),
                            dataWrapper.getPriority().get(j));
                } catch (IllegalArgumentException e){
                    System.out.println("Ошибка!" + e.getMessage());
                }
                break;
            case "Описание":
                System.out.print("Введите описание: ");
                String newDescription = scanner.nextLine();
                try{
                    dataWrapper.getDescription().set(j, newDescription);
                    System.out.println("Ваши изменения:");
                    Updates(dataWrapper.getUniqueNum().get(j), dataWrapper.getHeading().get(j), newDescription,
                            dataWrapper.getTime().get(j), dataWrapper.getPriority().get(j));
                }catch (IllegalArgumentException e){
                    System.out.println("Ошибка!" + e.getMessage());
                }
                break;
            case "Дата":
                System.out.print("Введите дату: ");
                String newTime = scanner.nextLine();
                try{
                    dataWrapper.setTime(j, newTime);
                    System.out.println("Ваши изменения:");
                    Updates(dataWrapper.getUniqueNum().get(j), dataWrapper.getHeading().get(j),
                            dataWrapper.getDescription().get(j), newTime,
                            dataWrapper.getPriority().get(j));
                }catch (IllegalArgumentException e){
                    System.out.println("Ошибка!" + e.getMessage());
                }
                break;
            case "Приоритет":
                System.out.print("Введите приоритет: ");
                String newPriority = scanner.nextLine();
                try{
                    dataWrapper.setPriority(j, newPriority);
                    System.out.println("Ваши изменения:");
                    Updates(dataWrapper.getUniqueNum().get(j), dataWrapper.getHeading().get(j),
                            dataWrapper.getDescription().get(j),dataWrapper.getTime().get(j) ,
                            newPriority);
                }catch (IllegalArgumentException e){
                    System.out.println("Ошибка!" + e.getMessage());
                }
                break;

            default:
                System.out.println("Ошибка! Пожалуйста, проверьте корректно ли вы ввели данные.");

        }
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(dataWrapper, writer);
            System.out.println("Изменения сохранены!");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения!" + e.getMessage());
        }

    }public void Delition() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Чтобы удалить задачу, введите уникальный номер.");
        System.out.print("Уникальный номер: ");
        String UniqueN = scanner.nextLine();
        boolean flag = false;
        int j = -1;
        List<String> Num = dataWrapper.getUniqueNum();
        for (int i = 0; i < Num.size(); i++){
            if (Num.get(i).equals(UniqueN)){
                j = i;
                flag = true;
                break;
            }
        }
        if (!flag){
            System.out.println("Проверьте корректно ли введены данные.");
            return;
        }
        dataWrapper.getUniqueNum().remove(j);
        dataWrapper.getHeading().remove(j);
        dataWrapper.getDescription().remove(j);
        dataWrapper.getTime().remove(j);
        dataWrapper.getPriority().remove(j);

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(dataWrapper, writer);
            System.out.println("Задача удалена!");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения!" + e.getMessage());
        }

    } public void Updates(String uniqueNum, String heading, String description, String time, String priority) {
        System.out.println("{");
        System.out.println("  \"id\": \"" + uniqueNum + "\",");
        System.out.println("  \"Заголовок\": \"" + heading + "\",");
        System.out.println("  \"Описание\": \"" + description + "\",");
        System.out.println("  \"Дата\": \"" + time + "\",");
        System.out.println("  \"Приоритет\": \"" + priority + "\"");

        System.out.println("}");

    }public void Output() {

        List<String> lNumbers = dataWrapper.getUniqueNum();
        List<String> lHeadings = dataWrapper.getHeading();
        List <String> lDescriptions = dataWrapper.getDescription();
        List <String> lTimes = dataWrapper.getTime();
        List <String> lPrioritys = dataWrapper.getPriority();

        if (lHeadings.isEmpty()){
            System.out.println("На данный момент задач нет. Добавьте задачу!");
            return;
        }
        System.out.println("[");
        for (int i = 0; i < lHeadings.size(); i++) {
            System.out.println("  \"Уникальный номер\": \"" + lNumbers.get(i) + "\",");
            System.out.println("  \"Заголовок\": \"" + lHeadings.get(i) + "\",");
            System.out.println("  \"Описание\": \"" + lDescriptions.get(i) + "\",");
            System.out.println("  \"Дата\": \"" + lTimes.get(i)+ "\",");
            System.out.println("  \"Приоритет\": \"" + lPrioritys.get(i) + "\"");
            System.out.println(" }" + (i < lHeadings.size() - 1 ? "," : ""));
        } System.out.println("]");

    }public void Sort(){
        class PriorityHelper {
            int Weight(String priority) {
                if (priority == null) return 4;
                switch (priority) {
                    case "высокий":
                        return 1;
                    case "средний":
                        return 2;
                    case "низкий":
                        return 3;
                    default:
                        return 4;
                }
            }
        }

        PriorityHelper helper = new PriorityHelper();

        System.out.println("Введите критерий сортировки. Сортировка может быть совершена по двум критериям: 1. Приоритет/ 2. Дата.");
        System.out.print("Ваш критерий: ");
        Scanner scanner = new Scanner(System.in);
        int criteria = scanner.nextInt();
        scanner.nextLine();

        List<Unification> objects = new ArrayList<>();
        for (int i = 0; i < dataWrapper.getUniqueNum().size(); i++) {
            objects.add(new Unification( dataWrapper.getUniqueNum().get(i),
                    dataWrapper.getHeading().get(i), dataWrapper.getDescription().get(i),
                    dataWrapper.getTime().get(i), dataWrapper.getPriority().get(i)));
        }
        Comparator<Unification> comparator;


        switch (criteria) {
            case 1:
                comparator = (a, b) -> helper.Weight(a.priority) - helper.Weight(b.priority);
                Collections.sort(objects, comparator);
                System.out.println("Ваши задачи отсортированные по приоритету: ");
                OutputSortion(objects);
                break;
            case 2:
                comparator = (a, b) -> {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                        Date first = format.parse(a.time);
                        Date second = format.parse(b.time);
                        return first.compareTo(second);
                    }catch (ParseException e) {
                        return 0;
                    }
                };
                Collections.sort(objects, comparator);
                System.out.println("Ваши задачи отсортированные по дате: ");
                OutputSortion(objects);
                break;
            default:
                System.out.println("Неверный выбор!");
                return;

            }

    } public void OutputSortion(List<Unification> objects) {
        if (objects.isEmpty()) {
            System.out.println("На данный момент задач нет. Добавьте задачу!");
            return;
        }

        System.out.println("[");
        for (int i = 0; i < objects.size(); i++) {
            Unification object = objects.get(i);
            System.out.println("  {");
            System.out.println("    \"Уникальный номер\": \"" + object.id + "\",");
            System.out.println("    \"Заголовок\": \"" + object.heading + "\",");
            System.out.println("    \"Описание\": \"" + object.description + "\",");
            System.out.println("    \"Дата\": \"" + object.time + "\",");
            System.out.println("    \"Приоритет\": \"" + object.priority + "\"");
            System.out.println("  }" + (i < objects.size() - 1 ? "," : ""));
        }
        System.out.println("]");
    }public void Search(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите критерий поиска: ");
        String criteria = scanner.nextLine().toLowerCase();
        boolean flag = false;
        for (int i = 0; i < dataWrapper.getHeading().size(); i++){
            String heading = dataWrapper.getHeading().get(i).toLowerCase();
            String description = dataWrapper.getDescription().get(i).toLowerCase();
            String time = dataWrapper.getTime().get(i).toLowerCase();
            String priority = dataWrapper.getPriority().get(i).toLowerCase();


            if (heading.contains(criteria) || description.contains(criteria) ||
                    time.contains(criteria) || priority.contains(criteria)) {
                    System.out.println("Задачи, удовлетворяющиее ващему поиску: ");
                    Updates(dataWrapper.getUniqueNum().get(i), dataWrapper.getHeading().get(i),
                            dataWrapper.getDescription().get(i), dataWrapper.getTime().get(i),
                            dataWrapper.getPriority().get(i));
                    System.out.println(" ");
                    flag = true;

            }
        }if (!flag) {
        System.out.println("К сожалению задача не найдена. Проверьте корректность ввода.");
        }

    }

}



