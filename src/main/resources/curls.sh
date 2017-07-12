#Insert 1
curl -X POST http://localhost:5000/dropwizard/supplier/product/add -H 'content-type: application/json' -d '{ "name": "Mac book Pro", "description": "A Nice laptop by apple", "quantity": 10, "price": 50000 }'

#Insert 2
curl -X POST http://localhost:5000/dropwizard/supplier/product/add -H 'content-type: application/json' -d '{ "name": "Mac book Air", "description": "A Nice Lighter laptop by apple", "quantity": 5, "price": 60000 }'

#Insert 3
curl -X POST http://localhost:5000/dropwizard/supplier/product/add -H 'content-type: application/json' -d '{ "name": "iPhone 6S", "description": "The best phone ever", "quantity": 15, "price": 55000 }'

#Update Price
curl -X POST http://localhost:5000/dropwizard/supplier/product/change_price/1/120000

#Update quantity
curl -X POST http://localhost:5000/dropwizard/supplier/product/change_quantity/1/100


#Purchase
curl -X POST http://localhost:5000/dropwizard/user/purchase -H 'content-type: application/json' -d '{ "userId": "sandeep", "cashPaid": "100000", "productQuantity":{ "1":"1", "2":"1" } }'

#Add cash
curl -X POST http://localhost:5000/dropwizard/supplier/cash/add/500 -H 'content-type: application/json'

#Remove cash
curl -X POST http://localhost:5000/dropwizard/supplier/cash/remove/500 -H 'content-type: application/json'

#Get Statement
curl -XGET http://localhost:5000/dropwizard/supplier/statement

#Revert a purchase
curl -X POST http://localhost:5000/dropwizard/user/purchase/cancel/1 -H 'content-type: application/json'

#Reset Machine.
curl -X POST http://localhost:5000/dropwizard/supplier/machine/reset -H 'content-type: application/json'
