package ua.euler.javaclass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExampleApp {

    /* @SneakyThrows
   public static void main(String[] args) {

        FileReader fileReader = new FileReader(ExampleApp.class.getResource("test.csv").getFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<Quaternion> quaternionList = new ArrayList<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.replaceAll(",", ".").replaceAll(";", ",");
            String[] split = line.split(",");

           Quaternion quaternion = new Quaternion(Double.parseDouble(split[0]),
                    Double.parseDouble(split[1]),
                    Double.parseDouble(split[2]),
                    Double.parseDouble(split[3]));
            quaternionList.add(quaternion);
        }

        List<EulerAngles> eulerAngles = QuaternionToEulerAnglesConvector.quaternionToEulerAnglesBulk(quaternionList);

        FileWriter fileWriter = new FileWriter("output.csv", true);
        for (EulerAngles eulerAngle: eulerAngles) {
            log.info(eulerAngle.toString());
            fileWriter.write(eulerAngle.toString());
        }

        fileReader.close();
        bufferedReader.close();
        fileWriter.close();
    } */

}
