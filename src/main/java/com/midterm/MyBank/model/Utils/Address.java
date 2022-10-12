package com.midterm.MyBank.model.Utils;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Optional;

@Embeddable
public class Address {
        private String country;
        private String city;
        private String line1;
        private String line2;

        //constructor
        public Address(){

        }
        public Address(String country, String city, String line1, String line2){
                this.country = country;
                this.city = city;
                this.line1 = line1;
                if (!(line2 == null)) {this.line2 = line2;}
        }

}
