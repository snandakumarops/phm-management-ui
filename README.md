# Population Health Management UI Application

This is a simple UI application which will be used to demonstrate the [Population Health Management System Demo](https://github.com/mauriziocarioli/PHM-Processes)

This application has two UI screens. One provides an administrative view which can be used to demonstrate the login for a Doctor(Peter), Charlie(Insurance Channel Worker) and Robert(Pharmacist).
The screen can be accessed at http://localhost:8037/static/phm_management.html

![](https://raw.githubusercontent.com/snandakumar87/phm-management-ui/master/src/main/resources/readmeimages/PHM_admin.png)

The `Logged in User` lets us switch between the 3 different users. For example, when choosing the Doctor's perspective, you will see the screen as below.

![](https://raw.githubusercontent.com/snandakumar87/phm-management-ui/master/src/main/resources/readmeimages/PHM_admin_doc.png)

The second UI screen provides a customer view which is more informational in nature.
This page can be accesed at http://localhost:8037/static/customer_view.html

![](https://raw.githubusercontent.com/snandakumar87/phm-management-ui/master/src/main/resources/readmeimages/PHM_customer.png)

##Setup

```
git clone https://github.com/snandakumar87/phm-management-ui
cd phm-management-ui
mvn clean install
java -jar target/phm-process-ui-1.0.0-fat.jar
```

##Additional notes

The UI application currently points to localhost:8080 for the business central URL and has the following users setup (Peter, Robert, Charlie and Mary). Make sure to adjust
the URL and the credentials according to the environment.