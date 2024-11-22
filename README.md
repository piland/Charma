<!-- Referenced othneildrew/Best-README-Template for inspiration -->


<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->


<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->


<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/piland/Charma.git">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Charma</h3>

  <p align="center">
    Geographical Service Application for University of North Carolina at Charlotte community
  </p>
</div>

<hr>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
      </ul>
    </li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<hr>

<!-- ABOUT THE PROJECT -->
## About The Project

Charma is an android application that provides geographical locations of the University of North Carolina at Charlotte. It provides features such as:
<ul>
  <li>Rout planning across campus</li>
  <li>Contacting campus security</li>
  <li>Looking at upcoming campus events</li>
  <li>Looking at recent campus news</li>
  <li>Saving your favorite locations</li>
  <li>And more!</li>
</ul>

This project is developed as part of ITSC-4155

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<hr>

### Built With

* Android Studio ...Need to add picture and link
* Jetpack Compose ...Need to add picture and link
* Google Maps Api ...Need to add picture and link
* Firebase ...Need to add picture and link

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<hr>

<!-- GETTING STARTED -->
## Getting Started

To install Charma, from the command line:
```bash
    # Clone Charma Repository
    $ git clone https://github.com/piland/Charma.git
```
<ol>
  <li>
    Open the project in your chosen IDE and configure the required API keys for Firebase and Google Maps
  </li>
  <li>For the Google Maps API</li>
  <ol>
    <li>If using Android Studio: Go into the AndroidManifest.xml and find the "<meta-data>"</li>
      <li>There will be an API key value and assign the Google API Key to the android:value variable
      </li>
  </ol>
  <li>In your Firebase Account Settings:</li>
      <ul>
        <li>Find 'Your Apps' and install the google-services.json SDK</li>
      </ul>
    <li>Take the downloaded file and move it into the App folder of the Charma project</li>
</ol>


### Prerequisites

This application was designed for the University of North Carolina Community and all UI is meant to represent the University.
For Technologies:
<p>You will need a <a href="https://firebase.google.com/">Firebase</a> account and a <a href="https://firebase.google.com/">Google</a> Maps API key.</p>



<!-- ROADMAP -->
## Roadmap

- [ ] Save Favorite Locations
- [ ] Multi-Factor Authentication
- [ ] Contact Campus Security
- [ ] ... More will be added during development


<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Ethan Piland - pilandethan@gmail.com
<br>
Jason Lindsey - novasquadcpu@gmail.com
<br>
Trevor Portillo - newtrevord@gmail.com
<br>
Wuilmer Velasquez - ...
<br>
Matthew McCall - ...
<br>

Project Link: [github]

<p align="right">(<a href="#readme-top">back to top</a>)</p>




<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[github]: https://github.com/piland/Charma
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_username
[AndroidStudioLogo]: data:image/png;base64,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
[android-url]: https://developer.android.com/studio
[Jetpack Compose-logo]: data:image/png;base64,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
[compose-url]: https://developer.android.com/compose
[GoogleMaps-logo]: data:image/png;base64,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
[GoogleMapsAPI-url]: https://developers.google.com/maps
[DuoPush-logo]: https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTb-YujoN-x75F81CvbrdHG6c-eH82gbfNt6w&s
[DuoPush-url]: https://duo.com/resources/ebooks/the-multi-factor-authentication-evaluation-guide?utm_source=google&utm_medium=paid_search&utm_campaign=DUO_AMER_NA_GS_Branded_General_T2&utm_content=General&_bt=653430164128&_bk=duo%20push&_bm=e&_bn=g&_bg=146748780197&gad_source=1&gclid=Cj0KCQjwjNS3BhChARIsAOxBM6opBnCeXmgeWUMHVxsLo_jo-ukh8OckqfAi9Xi9rG2CSjekltzcb2kaAv4yEALw_wcB
