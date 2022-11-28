<template>
    <div class="myDonations">
        <head>
            <link href="https://fonts.googleapis.com/css?family=Inter&display=swap" rel="stylesheet" />
        </head>
        <div class="circle"></div>
        <div class="upperMask"></div>
        <div class="lowerMask"></div>
        <div class="donateButton"></div>
        <span class="donateButtonTxt">DONATE</span>
        <span class="title">MY DONATIONS</span>
        <div class="bookmark"></div>
        <span class="thankYou">Thank you for your wonderful donations!</span>
    </div>
</template>

<style scoped>
.myDonations {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.circle {
  width: 100%;
  height: 100%;
  background: rgba(236,236,236,1);
  opacity: 1;
  position: absolute;
  top: 324px;
  left: 20px;
  border-radius: 50%;
}

.lowerMask {
  width: 100%;
  height: 162px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: absolute;
  top: 862px;
  left: 0px;
  overflow: hidden;
}

.upperMask {
  width: 100%;
  height: 290px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: absolute;
  top: 1px;
  left: 5px;
  overflow: hidden;
}

.donateButton {
  width: 170px;
  height: 46px;
  background: rgba(0,133,115,1);
  opacity: 1;
  position: absolute;
  top: 53px;
  left: 1214px;
  border-top-left-radius: 50px;
  border-top-right-radius: 50px;
  border-bottom-left-radius: 50px;
  border-bottom-right-radius: 50px;
  overflow: hidden;
}

.donateButtonTxt {
  width: 77px;
  color: rgba(255,255,255,1);
  position: absolute;
  top: 65px;
  left: 1261px;
  font-family: Inter;
  font-weight: Extra Bold;
  font-size: 18px;
  opacity: 1;
  text-align: center;
}

.title {
  width: 316px;
  color: rgba(0,133,115,1);
  position: absolute;
  top: 49px;
  left: 167px;
  font-family: Inter;
  font-weight: Bold;
  font-size: 40px;
  opacity: 1;
  text-align: center;
}

.bookmark{
  width: 73px;
  height: 129px;
  background: rgba(213,210,210,1);
  opacity: 1;
  position: absolute;
  top: 0px;
  left: 70px;
  overflow: hidden;
}

.thankYou {
  width: 796px;
  color: rgba(0,0,0,1);
  position: absolute;
  top: 218px;
  left: 319px;
  font-family: Inter;
  font-weight: Bold;
  font-size: 40px;
  opacity: 1;
  text-align: center;
}
</style>

