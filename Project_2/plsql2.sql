--PACKAGE WITH ALL TRIGGERS AND PROCEDURES


set serveroutput on;

DROP PACKAGE project2;
DROP TRIGGER purchases_to_insert;
DROP TRIGGER supply_to_insert;
DROP TRIGGER logs_to_insert;
DROP TRIGGER log_purchases_insert;
DROP TRIGGER log_products_qoh;
DROP TRIGGER log_supply_insert;
DROP TRIGGER log_customers_visitsmade;
DROP TRIGGER purchases_insert_qoh;
DROP TRIGGER update_qoh;

--------------------------------------------------------------------------
/*Creating sequence for inserting data into Purchases table */

  CREATE SEQUENCE purchases_seq
  MINVALUE 1
  START WITH 500000
  INCREMENT BY 1
  NOCACHE;
   

------------------------------------------------------------------------
 /* Creating sequence on sup# on supply table */ 
  
  CREATE SEQUENCE supply_seq
  MINVALUE 1
  START WITH 2000
  INCREMENT BY 1
  NOCACHE;  

-------------------------------------------------------------------------
  /* Creating sequence on log# on logs table */
  CREATE SEQUENCE logs_seq
  MINVALUE 1
  START WITH 30000
  INCREMENT BY 1
  NOCACHE;
  
 
-----------------------------------------------------------------------
CREATE OR REPLACE PACKAGE project2
AS
-- procedure for question2

PROCEDURE show_employees		(e_recordset OUT SYS_REFCURSOR);
PROCEDURE show_customers	    (c_recordset OUT SYS_REFCURSOR);
PROCEDURE show_products			(pr_recordset OUT SYS_REFCURSOR);
PROCEDURE show_purchases        (p_recordset OUT SYS_REFCURSOR);
PROCEDURE show_suppliers		(s_recordset OUT SYS_REFCURSOR);
PROCEDURE show_supply	        (sp_recordset OUT SYS_REFCURSOR);
PROCEDURE show_logs				(l_recordset OUT SYS_REFCURSOR);

-- procedure for question3

PROCEDURE report_monthly_sale(p_id IN varchar2,p_recordset OUT SYS_REFCURSOR);

-- procedure for question4
-- procedure for insert into purchases
 PROCEDURE insert_purchases(e_id in PURCHASES.EID%TYPE,p_id in PURCHASES.PID%TYPE,c_id in PURCHASES.CID%TYPE,qty_purc in PURCHASES.QTY%TYPE,show_message OUT varchar);

-- procedure for insert into products

PROCEDURE insert_products( 
p_id IN PRODUCTS.PID%TYPE,
p_name IN PRODUCTS.PNAME%TYPE,
q_oh IN PRODUCTS.QOH%TYPE,
q_oh_threshold IN PRODUCTS.QOH_THRESHOLD%TYPE,
orig_price IN PRODUCTS.ORIGINAL_PRICE%TYPE,
discnt_rt IN PRODUCTS.DISCNT_RATE%TYPE,show_message OUT varchar);

END project2;
/

CREATE OR REPLACE PACKAGE BODY project2
AS

-- Procedure for question 2
	PROCEDURE show_employees
	(e_recordset OUT SYS_REFCURSOR)
	AS
	BEGIN
	OPEN e_recordset FOR
	SELECT * FROM employees;
	END;
	
	PROCEDURE show_customers
	(c_recordset OUT SYS_REFCURSOR)
	AS
	BEGIN
	OPEN c_recordset FOR
	SELECT * FROM customers;
	END;
	
	PROCEDURE show_products
	(pr_recordset OUT SYS_REFCURSOR)
	AS
	BEGIN
	OPEN pr_recordset FOR
	SELECT * FROM products;
	END;
	
	PROCEDURE show_purchases
	(p_recordset OUT SYS_REFCURSOR)
	AS
	BEGIN
	OPEN p_recordset FOR
	SELECT * FROM purchases;
	END;
	
	PROCEDURE show_suppliers
	(s_recordset OUT SYS_REFCURSOR)
	AS
	BEGIN
	OPEN s_recordset FOR
	SELECT * FROM suppliers;
	END;
	
	PROCEDURE show_supply
	(sp_recordset OUT SYS_REFCURSOR)
	AS
	BEGIN
	OPEN sp_recordset FOR
	SELECT * FROM supply;
	END;
	
	PROCEDURE show_logs
	(l_recordset OUT SYS_REFCURSOR)
	AS
	BEGIN
	OPEN l_recordset FOR
	SELECT * FROM logs;
	END;

-- Procedure for question 3
	
	PROCEDURE report_monthly_sale(p_id IN varchar2,p_recordset OUT SYS_REFCURSOR)
AS	  
p_id_count number := 0;
BEGIN
select count(*) into p_id_count from products where pid = p_id;
--EXCEPTION
	   --WHEN NO_DATA_FOUND THEN
		 -- DBMS_OUTPUT.PUT_LINE('product ID: ' || p_id || ' not found.');
  OPEN p_recordset FOR
        SELECT pname, to_char(ptime, 'Mon-YYYY') as Mon , sum(qty) as tot_qty , sum(total_price) as tot_price , sum(total_price)/sum(qty) as Avg_sale_price
	    FROM products prod JOIN purchases purc ON prod.pid=purc.pid WHERE prod.pid=p_id GROUP BY pname , to_char(ptime, 'Mon-YYYY') having sum(qty) > 0;
	END;

-- procedure for question4
-- procedure for insert into purchases
PROCEDURE insert_purchases(
e_id in PURCHASES.EID%TYPE,
p_id in PURCHASES.PID%TYPE,
c_id in PURCHASES.CID%TYPE,
qty_purc in PURCHASES.QTY%TYPE,show_message OUT varchar)
IS
e_id_count number := 0;
p_id_count number := 0;
c_id_count number := 0;
Min_supply SUPPLY.QUANTITY%TYPE;
qoh_threshold_src PRODUCTS.QOH_THRESHOLD%TYPE;
qoh_available PRODUCTS.QOH%TYPE;
qty_ordered SUPPLY.QUANTITY%TYPE; 
qoh_diff PRODUCTS.QOH%TYPE;
qoh_updated PRODUCTS.QOH%TYPE;
       
BEGIN
 SELECT qoh_threshold INTO qoh_threshold_src FROM PRODUCTS WHERE pid=p_id;
 SELECT qoh INTO qoh_available FROM PRODUCTS WHERE pid= p_id;
 
 qoh_diff:= qoh_available-qty_purc;
 Min_supply:= qoh_threshold_src - qoh_available -1;
 qty_ordered:=Min_supply+qoh_available+5;
 qoh_updated:= qoh_diff+qty_ordered;

--check if valid employee,product and customer is selected
 select count(*) into e_id_count from employees where eid = e_id;
  select count(*) into p_id_count from products where pid = p_id;
  select count(*) into c_id_count from customers where cid = c_id;
  
  IF e_id_count = 0 then
    show_message := 'The employee id is invalid';
	
	 ELSIF p_id_count = 0 then
    show_message := 'The product id is invalid';
	
	 ELSIF c_id_count = 0 then
    show_message := 'The customer id is invalid';

	ELSE
     FOR tot_price IN (SELECT qty_purc * (original_price*(1-discnt_rate)) as tot_price from products where pid=p_id)
     LOOP   
      INSERT INTO purchases (eid, pid, cid , qty , ptime , total_price) 
      VALUES (e_id, p_id, c_id, qty_purc, sysdate, tot_price.tot_price );
	  IF qoh_diff < qoh_threshold_src
	                THEN show_message := 'Values are inserted into Purchases tables successfully. Available quantity is less than threshold.Requesting new Supply... New QOH value-' ||qoh_updated;
				  ELSE   
					show_message := 'Data inserted into Purchases table successfully. No additional supply required';
	   END IF;
     END LOOP;
   COMMIT;
   
END IF;
END;

--procedure for insert into products
PROCEDURE insert_products( 
p_id IN PRODUCTS.PID%TYPE,
p_name IN PRODUCTS.PNAME%TYPE,
q_oh IN PRODUCTS.QOH%TYPE,
q_oh_threshold IN PRODUCTS.QOH_THRESHOLD%TYPE,
orig_price IN PRODUCTS.ORIGINAL_PRICE%TYPE,
discnt_rt IN PRODUCTS.DISCNT_RATE%TYPE,show_message OUT varchar)
IS
BEGIN
   INSERT INTO PRODUCTS (pid, pname, qoh, qoh_threshold, original_price, discnt_rate)
   VALUES (p_id, p_name, q_oh, q_oh_threshold, orig_price, discnt_rt);
   COMMIT;
    show_message := 'The values are inserted';
END;
END project2;
/
show errors;



-- Creation of all the triggers needed for Project 2.

--Creating trigger to auto increment the sequence up on inserts (1)

 CREATE OR REPLACE TRIGGER purchases_to_insert
  BEFORE INSERT ON purchases
  FOR EACH ROW
BEGIN
  SELECT purchases_seq.nextval
  INTO :new.pur#
  FROM dual;
END;
/
-- Creating trigger to auto increment the sequence up on inserting data in supply table (1)
  
 CREATE OR REPLACE TRIGGER supply_to_insert
  BEFORE INSERT ON supply
  FOR EACH ROW
BEGIN
  SELECT supply_seq.nextval
  INTO :new.sup#
  FROM dual;
END;
/
-- Creating a trigger to auto increment log# in logs table for every new tuple inserted (1)
  
  CREATE OR REPLACE TRIGGER logs_to_insert
  BEFORE INSERT ON logs
  FOR EACH ROW
BEGIN
  SELECT logs_seq.nextval
  INTO :new.log#
  FROM dual;
END;
/
-- Creating a trigger to insert values in logs after inserting into purchases (5)

CREATE OR REPLACE TRIGGER log_purchases_insert
  AFTER INSERT ON purchases
  FOR EACH ROW
DECLARE v_username varchar2(20);
BEGIN
    SELECT user INTO v_username from dual;
    INSERT INTO logs (who,otime,table_name,operation,key_value)
	values (v_username,sysdate, 'Purchases', 'Insert', :NEW.pur#);
END;
/
-- Creating a trigger to insert values in logs after updating qoh into products (5)

CREATE OR REPLACE TRIGGER log_products_qoh
  AFTER UPDATE OF qoh ON products
  FOR EACH ROW
DECLARE v_username varchar2(20);
BEGIN
  SELECT user INTO v_username from dual;
  INSERT INTO logs (who,otime,table_name,operation,key_value)
  VALUES (v_username,sysdate, 'Products', 'Update', :OLD.pid);
END;
/
-- Creating a trigger to insert values in logs after inserting into supply table (5)
CREATE OR REPLACE TRIGGER log_supply_insert
  AFTER INSERT ON Supply
  FOR EACH ROW
DECLARE v_username varchar2(20);
BEGIN
  SELECT user INTO v_username from dual;
  INSERT INTO logs (who, otime, table_name, operation, key_value)
  VALUES (v_username, sysdate , 'Supply', 'Insert', :NEW.sup#);
END;
/

-- Creating a trigger to insert values in logs after updating visits made in customers (5)
CREATE OR REPLACE TRIGGER log_customers_visitsmade
  AFTER UPDATE OF visits_made on Customers
  FOR EACH ROW
 DECLARE v_username varchar2(20);
BEGIN
  SELECT user INTO v_username from dual;
  INSERT INTO logs (who, otime, table_name, operation, key_value)
  VALUES (v_username, sysdate , 'Customers','Update',:OLD.cid);
END;
/
-- Creating a trigger to qoh BEFORE INSERT ON PURCHASES (6)

CREATE OR REPLACE TRIGGER purchases_insert_qoh
  BEFORE INSERT ON PURCHASES
  FOR EACH ROW
DECLARE
  qoh_existing PRODUCTS.QOH%TYPE;
  exp EXCEPTION;
BEGIN
   SELECT qoh INTO qoh_existing FROM products WHERE pid = :new.pid;
   IF (qoh_existing< :new.qty) THEN
     RAISE exp; 
   END IF;
  EXCEPTION
    WHEN exp THEN
 	RAISE_APPLICATION_ERROR( -20020,'Insufficient Quantity in Stock' );
 end;
/
-- Creating a trigger to qoh  (7)


CREATE OR REPLACE TRIGGER update_qoh
  AFTER INSERT ON PURCHASES
  FOR EACH ROW
DECLARE
qoh_available PRODUCTS.QOH%TYPE;
qoh_diff PRODUCTS.QOH%TYPE;
qoh_threshold_src PRODUCTS.QOH_THRESHOLD%TYPE;
qoh_updated PRODUCTS.QOH%TYPE;
supplier_sid SUPPLIERS.SID%TYPE;
new_qoh PRODUCTS.QOH%TYPE;
visits_made_existing CUSTOMERS.VISITS_MADE%TYPE;
visits_made_new CUSTOMERS.VISITS_MADE%TYPE;
Min_supply SUPPLY.QUANTITY%TYPE;
qty_ordered SUPPLY.QUANTITY%TYPE; 
BEGIN
   SELECT qoh INTO qoh_available FROM PRODUCTS WHERE pid= :NEW.pid;
   qoh_diff:= qoh_available-:NEW.qty;
   SELECT qoh_threshold INTO qoh_threshold_src FROM PRODUCTS WHERE pid= :NEW.pid;
   UPDATE products SET qoh=qoh_diff WHERE pid= :NEW.pid;
   if(qoh_diff<qoh_threshold_src) then
        ---print msg when there is insufficient quantity
           DBMS_OUTPUT.PUT_LINE('Existing supply is not sufficient to complete the request.New supply is requested.');
           select sid into supplier_sid from (select * from supply where pid=:new.pid  order by sid asc)s where rownum=1;
		----Defining a minimum amount to order
           Min_supply:= qoh_threshold_src - qoh_available -1;
           qty_ordered:=Min_supply+qoh_available+5;
		---Orderign new supply as demand is more than what is available(qoh)
         INSERT INTO SUPPLY (pid,sid,sdate,quantity)
         VALUES (:new.pid, supplier_sid, sysdate, qty_ordered);
           qoh_updated:= qoh_diff+qty_ordered;
         UPDATE products SET qoh=qoh_updated WHERE pid=:NEW.pid;
         SELECT qoh into new_qoh FROM PRODUCTS WHERE pid=:NEW.pid;
		 ---This is the new Qoh after receiving supply
         DBMS_OUTPUT.PUT_LINE('New Supply is available. Quantity is hand for the product is:' ||new_qoh );
		 ---Updating the existing qoh after the purchase is placed.
   END IF;
   SELECT visits_made INTO visits_made_existing FROM customers WHERE cid=:NEW.cid;
   visits_made_new:=visits_made_existing+1;
   UPDATE customers SET visits_made=visits_made_new WHERE cid=:NEW.cid;
   UPDATE customers SET last_visit_date=:NEW.ptime WHERE cid=:NEW.cid AND last_visit_date <> :NEW.ptime;
   
  -- EXCEPTION 
      --  WHEN exception_name THEN
       --   DBMS_OUTPUT.PUT_LINE('Existing supply is not sufficient to complete the request.New supply is requested.');
        --  DBMS_OUTPUT.PUT_LINE('New Supply is available. Quantity is hand for the product is:' ||new_qoh );		  
		
END;
/
show errors;

--refcursor variable needed for question 2

variable show_employees refcursor;
exec project2.show_employees ( :show_employees );
--print show_students

variable show_customers refcursor;
exec project2.show_customers ( :show_customers );
--print show_tas

variable show_products refcursor;
exec project2.show_products ( :show_products );
--print show_courses

variable show_purchases refcursor;
exec project2.show_purchases ( :show_purchases );
--print show_classes

variable show_suppliers refcursor;
exec project2.show_suppliers ( :show_suppliers );
--print show_enrollments

variable show_supply refcursor;
exec project2.show_supply ( :show_supply );
--print show_prerequisites

variable show_logs refcursor;
exec project2.show_logs ( :show_logs );
--print show_logs

--refcursor variable needed for question 3

variable report_monthly_sale refcursor;
 

























  
  
