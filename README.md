# LifeSaver
VE441 group project source 

## Documentation
> link to all 3rd-party tools, libraries, SDKs, APIs
### Front-end dependencies
- [Google Maps](https://developers.google.com/maps/documentation)\
  To activate google maps api, you need to add a key to your local.properties. This key need to be deleted when we launch the project.
  ```
  MAPS_API_KEY=AIzaSyAed11pVL9JPm-0TXGhHChmV6TcyXeyEJo
  ```
- [Google ARCore](https://developers.google.com/ar/develop?hl=zh-cn)
- [EastAR](https://www.easyar.com/view/support.html)
- [OpenCV4Android](https://docs.opencv.org/3.4/d9/d3f/tutorial_android_dev_intro.html)
- [OkHttp](https://github.com/square/okhttp)
### Back-end dependencies
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
![Engine Architecture](https://github.com/AlonsoChate/LifeSaver/blob/main/figures/EngineArchitecture.png)

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

## Team Roster
| Team Member | Contribution |
| :--- | :--- |
| Jiaai Xu    | App construction, add google Maps API |
| Kehan Chen  | Add CPR guide |
| Lan Zhang   | Display route function, app construction, add google Maps API |
| Wanying Ji  |  |
| Yiteng Cai  | Configure google map setting. Display route function |
| Yuyuan Ji   | App construction, add google Maps API |

## Individual Contribution
**Jiaai Xu**: Build the overall app structure (add and link the buttons and pages). Add and the Google Maps API to app.\
**Yuyuan Ji**: Build the overall app structure (add and link the buttons and pages). Add and the Google Maps API to app.\
**Lan Zhang**: Complete the display of route function. Build the overall app structure (add and link the buttons and pages). Add and the Google Maps API to app.\
**Yiteng Cai**: Configure Google map fragment setting. Implement function to get current location. Retrieve route info from origin and destination, and display route on the map.\
**Kehan Chen**: Add the CPR text and image guide.\
**P.S.** Since we have offline meetings, we sometimes use only one team member's labtop to develop the final product. So some of the members may not have the git commit repos to the code.
