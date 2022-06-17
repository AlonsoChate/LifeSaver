# LifeSaver
VE441 group project source 

## Documentation
> link to all 3rd-party tools, libraries, SDKs, APIs
### Front-end dependencies
- [Baidu map](https://lbsyun.baidu.com/index.php?title=androidsdk)
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

- Front end

- Main

- AR Handler

- ARCore

  ARCore 

- Map Handler

  The map handler directly deals with the BaiduMap API, which feeds user location into API and receive the navigation information.

- DB

  The DB (database) is used for storing `user profiles` and `AED locations`. `user profiles` include users' personal health conditions, emergency contacts, and personal information. `AED locations` are stored in the form API requires.

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
  - ???

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
> blank for now

## Team Roster
| Team Member | Contribution |
| :--- | :--- |
| Jiaai Xu    |  | 
| Kehan Chen  |  |
| Lan Zhang   |  |
| Wanying Ji  |  |
| Yiteng Cai  |  |
| Yuyuan Ji   |  |
