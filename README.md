# LifeSaver
VE441 group project source 





## Getting Started: how to build and run our project
### Front-end part
You will need two API keys to activate google map API and google ARcore cloud AR service. To activate google maps API, you need to add a key to your `local.properties` file, like the following example.
  ```
  MAPS_API_KEY=AIzaSyAed11pVL9JPm-0TXGhHChmV6TcyXeyEJo
  ```
Our key will soon get expired, you can also get a new key following the procedure here [get-api-key.](https://developers.google.com/maps/documentation/android-sdk/get-api-key)

As for ARcore cloud service, for simplicity, you can replace line 74 `val apiKey = ...` of file [`AppRenderer.kt`](https://github.com/AlonsoChate/LifeSaver/blob/main/app/src/main/java/com/example/ve441_lifesaver_draft/kotlin/ml/AppRenderer.kt) with your API key, like this
  ```
  val apiKey = AIzaSyBFthFwiCC3O6a6nfEuEkIToRz-m9krFTs
  ```
To enable http communication between front-end and back-end, you will need to install a self-signed CA certificate on your mobile phone. Download the our certificate `selfsigned.crt` from our back-end repo [LifeSaver BackEnd](https://github.com/lanzhgx/LifeSaver_Backend) and install it on your phone, or you can use your own certificate on your own server following the instructions on Back-end part.


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

While users click any button in the front end, the Main Activity will be activated. Based on the different action by users, Main Activity make requests to different handlers. If the request is to show an AR CPR, Main Activity will activate AR Handler and AR Handler will make requests to AR Core to retrieve the response. If the request is to navigate, Main Activity will first make requests to Back End to retrieve AED locations from DB and second activate Map Handler to make requests to Baidu Map API to retrieve the route. The map handler directly deals with the BaiduMap API, which feeds user location into the API and receives the navigation information. When using AR navigation, the map handler calls ARCore to display AR navigation view. The DB (database) is used for storing `user profiles` and `AED locations`. `user profiles` include users' personal health conditions, emergency contacts, and personal information. `AED locations` are stored in the form API requires.

## APIs and Controller
- Front End and Back End, using Okhttp
  - Send user profile and location
  - Retrieve several nearest AED locations
- Front End and Baidu Map, using Baidu Map API
  - Send user location and AED locations
  - Retrieve the route and displayable map
- Front End and Position Sensors, using [in-built Android API](https://developer.android.com/guide/topics/sensors/sensors_position)
  - Retrieve the device's orientation
- Front End and AR Module, using Google ARCore

### Endpoint: `GET /lifesaver/`

**Request Parameters**
| Key  | Location | Type  |Description  |
| :--- | :--- | :---  | :--- |
| username | Session Cookie | String | Current user | 
| password | Session Cookie | String | hash of password | 
| userinfo | JSON | List of dictionary | Lisf of user info with keys and values |
| location  | JSON |  | GPS location info |

**Response Codes**
| Code  | Description |
| :--- | :--- |
| `200 OK` | Success |
| `400 Bad ` | Invalid parameters |

**Returns**
| Key  | Location | Type  |Description  |
| :--- | :--- | :---  | :--- |
| AEDinfo | JSON | list of dictionary | Detail info of nearby AEDs including location, appearence(photo), maintanance, etc | 



## View UI/UX
![UIUX Flow](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/uiuxflow.png)
![UIUX-Navigation](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/uiux_navigation.png)
![UIUX-CPR Guide](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/uiux_cpr.png)
![UIUX Justification](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/uiux_justification.png)

## Individual Contribution
Lan Zhang, Yiteng Cai, Jiaai Xu, and Yuyuan Ji have offline meetings. We often tried to realize and debugged the functions individually and used only one team member's labtop to develop the final product. So some of the members may not have the git commit repos to the code. The specific division of work are listed as below. If two team members completed the same task (like Jiaai Xu and Yuyuan Ji), they shared the same effort.
**Lan Zhang**: Complete the display of route function. Build the overall app structure (add and link the buttons and pages). Add the Google Maps API to app. Help with ar function.\
**Yiteng Cai**: Configure Google map fragment setting. Implement function to get current location. Retrieve route info from origin and destination, and display route on the map.\
**Jiaai Xu**: Build the overall app structure (add and link the buttons and pages). Add the Google Maps API to app. Realize ar function. Add machine learning model to ar identify chest position. Help with the migration of code.\
**Yuyuan Ji**: Build the overall app structure (add and link the buttons and pages). Add the Google Maps API to app. Realize ar function. Add machine learning model to ar identify chest position. Help with the migration of code.\
**Kehan Chen**: Add the CPR text and image guide.\


## Team Roster
| Team Member | Contribution |
| :--- | :--- |
| Lan Zhang   | Display route function, app construction, add google Maps API |
| Yiteng Cai  | Configure google map setting, display route function, migrate the ar code to the main app |
| Jiaai Xu    | App construction, add google Maps API, realize ar function, add machine learning model to ar identify chest position |
| Yuyuan Ji   | App construction, add google Maps API, realize ar function, add machine learning model to ar identify chest position |
| Kehan Chen  | Add CPR guide |

