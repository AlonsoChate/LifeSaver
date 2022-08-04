# LifeSaver
VE441 group project source 





## Getting Started: how to build and run our project
### Front-end part
You will need two API keys to activate google map API and google ARcore cloud AR service. To activate google maps API, you need to add a key to your `local.properties` file, like the following example.
  ```
  MAPS_API_KEY=AIzaSyAed11pVL9JPm-0TXGhHChmV6TcyXeyEJo
  ```
As for ARcore cloud service, for simplicity, you can replace line 74 `val apiKey = ...` of file [`AppRenderer.kt`](https://github.com/AlonsoChate/LifeSaver/blob/main/app/src/main/java/com/example/ve441_lifesaver_draft/kotlin/ml/AppRenderer.kt) with your API key, like this
  ```
  val apiKey = AIzaSyBFthFwiCC3O6a6nfEuEkIToRz-m9krFTs
  ```
Our key will soon get expired, you can also get a new key following the procedure here [get-api-key](https://developers.google.com/maps/documentation/android-sdk/get-api-key) for Google map API and [here](https://cloud.google.com/vision/docs/setup) for Google ARcore.

To enable http communication between front-end and back-end, you will need to install a self-signed CA certificate on your mobile phone. Download the our certificate `selfsigned.crt` from our back-end repo [LifeSaver BackEnd](https://github.com/lanzhgx/LifeSaver_Backend) and install it on your phone, or you can use your own certificate on your own server.


### Back-end part
Our back-end is a modified version of the back-end used in lab. You can refer to the links below to know how to set up a server, how to configure your PostgreSQL database, and how to create and use your own CA certificate.

[lab1-chatter](https://eecs441.eecs.umich.edu/ji-asns/lab1-chatter), [lab2-chatter-with-images](https://eecs441.eecs.umich.edu/ji-asns/lab2-images)

As for the PostgreSQL database, you will need to configure the table with the command below
  ```
  CREATE TABLE aeds (id varchar(255), description varchar(255), location json);
  ```
and insert your values in this way
  ```
  INSERT INTO aeds VALUES ('test_aed0', 'Hello world', '{"Lat": "0.0", "Lng": "0.0"}');
  ```

As for the coding part in our server, you can refer to our [Backend repo](https://github.com/lanzhgx/LifeSaver_Backend). What's different from lab is that you need to set up the `getAED()` function in `app/views.py` and add the corresponding path in `routing/urls`.


### Dependencies
These are links to all 3rd-party tools, libraries, SDKs, APIs our project relies on directly.

#### Front-end dependencies
- [Google Maps](https://developers.google.com/maps/documentation)
- [Google ARCore](https://developers.google.com/ar/develop?hl=zh-cn)
- [OkHttp](https://github.com/square/okhttp)

#### Back-end dependencies
- [Python 3.8](https://www.python.org/downloads/)
- [PostgreSQL (Latest Version)](https://www.postgresql.org/docs/current/index.html)
- [Nginx (Latest Version)](https://nginx.org/en/docs/install.html)
- [Django 3.1.3](https://pypi.org/project/Django/)
- [Gunicorn (Latest Version)](https://pypi.org/project/gunicorn/)






## Model and Engine
## Storymap
![Storymap figure1](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/Storymap1.png)
![Storymap figure2](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/Storymap2.png)

## Engine Architecture
![Engine Architecture](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/LifeSaver%20Model%20and%20Engine.png)

While users click any button in the front end, the corresponding activity will be activated. Based on the different actions by users, the activity makes requests to different handlers. If the request is to show an AR CPR, AR Activity will activate AR Handler and AR Handler will make requests to Google AR Core to retrieve the response. If the request is to navigate, Map Activity will activate Map Handler which will make requests to Back End to retrieve AED locations and make requests to Google Maps API to retrieve the route.


## APIs and Controller
### Endpoint: `GET /getAEDs/`
**Response Codes**
| Code  | Description |
| :--- | :--- |
| `200 OK` | Success |
| `400 Bad ` | Invalid parameters |

**Returns**
| Key  | Value | Type  |Description  |
| :--- | :--- | :---  | :--- |
| AEDs | JSON | list of dictionary | Detail info of nearby AEDs including device id, description and location | 

In our app we need to post a `GET` request to the endpoint `/getAEDs/`. Our `getAEDs` API will send back the informations of AEDs in the form of a JSON object with the key being "AEDs" and the value being an array of string arrays. The first two strings are the id and description of the AED, and the last one is another JSON object consists the coordinate. For example: 
```
{
  "AEDs": [
      ["id0", "description0", "{"Lat": "0.0", "Lng": "0.0"}"], 
      [...],
      ...
  ]
}
```

### Google Maps SDK
Google maps is enabled with a API Key. We mainly utilize Google map SDK to display a map fragment in our map. We also use a Google map direction API to retrieve the route information from one location to another. A URL request consists of the origin and destination coordinate together with the API key is send to the Google map API endpoint `https://maps.googleapis.com/maps/api/directions/`, and the route info is returned as a JSON object. Detail information can be looked up [here](https://developers.google.com/maps/documentation/directions/get-directions). 

### Google AR Core SDK
We mainly utilize Google AR Core SDK to construct AR structure and to detect people's chest to be pressed during CPR. To retrieve the location of chest from Google's machine learning algorithms, we send a URL request, which consists of the preprocessed images, together with the API key to the endpoint `https://vision.googleapis.com/v1/images:annotate?key=$apiKey`, then the detection result is returned as a JSON objext. Detail information about the returned JSON can be found [here](https://cloud.google.com/vision/docs/reference/rest/v1/AnnotateImageResponse#LocalizedObjectAnnotation). Detail information about using custom model can be found [here](https://developers.google.com/ml-kit/vision/object-detection/custom-models/android).


## View UI/UX
### UI/UX Flow
![UIUX Flow](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/uiuxflow.png)
### Navigation
![UIUX-Navigation](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/uiux_navigation.png)
After clicking on `AED MAPS` on the main menu, users can get their current location by clicking on the radar icon on Pic 2 and see nearby AEDs by clicking on `SEARCH AED` in Pic 3. AEDs will be marked red, and when clicking on each AED, users will see its description. The route from user’s current location to the chosen AED, which is a blue curve, will be displayed if users select one AED and click on `GET ROUTE`. By clicking on the icon at the bottom right corner in Pic 6, the navigation will be started. Users will see their real-time location change and get real-time directions.
### CPR Guide
![UIUX-CPR Guide](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/uiux_cpr.png)
After clicking on `CPR GUIDES` on the main menu, detailed steps of CPR guidance will be shown. Users can go through these steps by clicking on `NEXT STEP` or `PREVIOUS STEP`. When giving chest compression (Step 2 in Pic 3), users can hit the camera icon on the top right corner to get AR guide on finding the correct chest compression position. In the camera interface as shown in Pic 6, make sure the person is in the shot, then hit `SCAN`, a red `press` will appear at the chest compression position.
### Design Decision Justification
<!--![UIUX Justification](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/uiux_justification.png)-->
In Navigation function, we changed the destination of the back arrow from the main menu to the previous page. Previously, users will return to the main menu after clicking the back arrow. According to the mockup usability test results, they think this design is not easy to use. After our modification, now they will return to the previous pages after clicking the back arrow. This makes our design less confusing and more convenient.

In CPR Guide function, previously, too many words in text guidance made users feel streesed. Now we make the text guidance more concise so that users can read it more easily. We also added image guidance on 2 critical steps in CPR to make it more intuitive. Besides, we changed the back buttons on the upper left corner to home buttons to make it less confusing. Now users use `NEXT STEP` and `PREVIOUS STEP` to read through CPR guidance, and use home button to return to the main menu.
In AR camera, we changed the destination of the `RETURN` button from the main menu to the previous page.


## Team Roster
Lan Zhang, Yiteng Cai, Jiaai Xu, and Yuyuan Ji have offline meetings. We often tried to realize and debugged the functions individually and used only one team member's laptop to develop the final product. So some of the members may not have the git commit repos to the code. The specific division of work are listed as below. If several team members completed the same task (like Jiaai Xu and Yuyuan Ji), they shared the same effort.
| Team Member | Contribution |
| :--- | :--- |
| Lan Zhang   | Implement the front end view, including main activity and car text guide activity. Embed Google Maps fragment into our map activity. Implement the back end server and database. |
| Yiteng Cai  | Mainly work on Map Activity. Configure Google map setting. Implement function to get current location. Retrieve route info from origin and destination, and display route on the map. Configure the back end to send-back AED info if requested. Migrate AR code to the main app.  |
| Jiaai Xu    | Build the overall app structure (add and link the buttons and pages). Add the Google Maps API to app. Realize ar function. Add machine learning model to ar identify chest position. Help with the migration of code. |
| Yuyuan Ji   | Build the overall app structure (add and link the buttons and pages). Add the Google Maps API to app. Realize ar function. Add machine learning model to ar identify chest position. Help with the migration of code. |
| Kehan Chen  | Add the CPR text and image guide. |

### Challenges encountered
To configure the google map fragment inside the map activity, we spend a lot of time looking up Google’s SDK document for Android. The first challenge we met is that many functions are not provided directly by Google SDK, so we have to implement them on our own. For example, Google map doesn’t provide direct navigation, so we have to send URL request, get route information, and manually add route. Moreover, much of the content in the official document is outdated, and still many functions are “deprecated”. As a result, we cannot directly use the code on the official platform. Instead, we need to look deeper into the code in the GitHub repo to judge which function can be used. The usage of AR in our app is also a big challenge. The quick intro on the official website mainly teaches people how to run their example code, so we have to read their example code in detail. Yet their example demo is quite complicated and consists of many separate files, which make it difficult for us to lean and write our own functions. As a result, we finally realize our function by modifying their example code. Immigrating the AR code to our main app is challenging as there are a lot of conflicting packages names and many new dependencies. It took much time to make it compatible with our app.

