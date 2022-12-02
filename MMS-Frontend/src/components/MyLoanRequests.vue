<template>
    <div class="myLoanRequests">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <span class="title">MY LOAN REQUESTS</span>
        <div class="bottomText">
        <span class="noOtherRequests">No other requests made.<br> Would you like to browse the catalog?<br></span>
        <form action="http://127.0.0.1:8087/#/">
        <button class="styledButton">View Catalog</button>
        </form>
        </div>
    </div>
</template>

<style scoped>
.myLoanRequests {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.noOtherRequests {
  width: 796px;
  color: rgba(0,0,0,1);
  font-family: Inter;
  font-weight: Bold;
  font-size: 40px;
  opacity: 1;
  text-align: center;
}

.bottomText {
  text-align: center;
  position:fixed;
  padding-bottom: 40px;
  left: 0;
  bottom: 0;
  right: 0;
}
</style>
