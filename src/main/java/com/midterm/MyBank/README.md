IMPORTANT: in AccountActions (Service->Utils) there is a method that has @PostConstruct commented out. If reinstated, it populates the database with several users an creates some accounts. The annotation "@PostConstruct" should be commented out or deleted after executing the app ONCE, otherwise it the app is executed with it twice it will create repeated users and accounts. Just to be clear, it should be executed only once.

Please execute from spring-boot:run

User case Diagram

![](User Case Diagram.PNG)

Class diagram

![](../../../../../../Class%20Diagram.png)

There are 4 types of accounts, Checking, studentChecking Credit Card, Savings. They inherit the class Account
There are 3 types of users: Admin, AccountHolder, and ThirdParty. They inherit the class User

Each User has a Role, a username and a Password that are used by Spring security to access the routes
Each account has a Repository, controller, and Service in order to do transfers, get, update and delete information.

-Third party users have an extra Service and Controller, in order to transfer funds to an existing accounts in a route. ThirdParty keys have to be saved without encryption in order to be able to compare them to the hashed key (as instructed) in the http route. In the route is also asked username (to check authorization), id of account, and account secret key.
-Accountholders have an extra Service and Controller, to get general info about all their accounts.

->Spring boot security:
-Accounthoders can only get or transfer funds from their account accessing the homepage gives them general details from their accounts in DTO form.
-Accountholders only can access their own accounts (username must match the one of the logged in user).
-Admins can access everything and they can modify balances, create and delete users.

There is a class called AccountActions with general methods like finding an account class, checking funds, etc.
For transfers, AccountActions checks type of receiving account, if emitter has enough funds, and adds funds to receiving account. Then the funds are substracted from emitting account in ServiceImpl.

Interest rate is applied when checking account, or making a transfer.
For yearly and monthly interest rate, the way is calculated is: instead of checking the creation date, the date where the fees where lastly applied is checked. Then it is checked if more than 30 days or 365 days have passed. If more time has passed, fee is applied for those days exactly. This is to set the date of last applied again and not overcomplicate it.
For example if I have 100€ in my account, interestFee 0.1, and a month and a half has passed the amount that will be added is: 0.1(rate)*100(balance), divided by 365(rate per day) and then multiplied by nº of days, 45 in this case.

Third parties can deposit

I have some routes saved in Postman, I can share them if needed! Sorry I did not have time to do more tests, I was focused in making everything work. I would have liked to implement a couple more things, but did not have time  (ノಠ益ಠ)ノ彡┻━┻ :

