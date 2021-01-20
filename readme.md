# A Java Version of Deck API

    This is a simple project based upon http://deckofcardsapi.com/

I've used some of their assets in here for the images, and looked at the api of the project in order to base my own.

---
# Usage:

Request a new Deck:
http://localhost:8080/api/deck/new/shuffle

* You can now pull cards off the top of this deck

<pre>
status	        "success"
shuffled	    "true"
deckId	        "c897761c-3974-417b-9532-66c9bf17b172"
remainingCards	"52"
</pre>

Peek at the top card you cheater!

http://localhost:8080/api/deck/<deckId>/peek

http://localhost:8080/api/deck/<deckId>/peek?count=52

* You can optionally pass a count value with a number less than the number of remainingCards in order to see that many.

<pre>
{
    "status":"success",
    "cards":[
        {
            "value":"EIGHT",
            "suite":"DIAMONDS",
            "shortCode":"8D",
            "imageUrl":"http://localhost:8080/images/8D.svg"
        }
            ],
    "deckId":"c897761c-3974-417b-9532-66c9bf17b172",
    "remainingCards":"52"
}
</pre>
Pull cards off the top
http://localhost:8080/api/deck/<deckId>/draw

http://localhost:8080/api/deck/<deckId>/draw?count=2

<pre>
{
    "status":"success",
    "cards":[
        {
            "value":"FOUR",
            "suite":"HEARTS",
            "shortCode":"4H",
            "imageUrl":"http://localhost:8080/images/4H.svg"
        }
    ],
    "deckId":"e92f8080-bd48-40c7-a2e9-b05a7ecd6711",
    "remainingCards":"48"
}
</pre>

You can shuffle a deck to randomize the contents: http://localhost:8080/api/deck/<deckId>/shuffle
<pre>
{
    deckId	"c897761c-3974-417b-9532-66c9bf17b172",
    status	"success",
    remainingCards	"52"
}
</pre>

One deck of cards not enough?

* You can create a new pile to have a subpile with your main deck:
    * http://localhost:8080/api/deck/<deckId>/pile/<pileName>/add/
    * http://localhost:8080/api/deck/<deckId>/pile/<pileName>/add/?cards=AH,QH
        * You can optionally fill it only with certain cards

<pre>
{
    remainingCards	"52"
    deckId	"25e0e2e4-bfe2-473a-99cf-828507938f6d"
    status	"success"
    piles
        pile_name
        status	"true"
        remainingCards	"1"
}
</pre>

Of course you can also draw and peek off the piles:

* http://localhost:8080/api/deck/<deckId>/pile/<pileName>/draw/
* http://localhost:8080/api/deck/<deckId>/pile/<pileName>/peek/

<pre>
	
remainingCards	"52"
deckId	"59263225-2cd8-46eb-856f-6b31c92bd9c2"
status	"success"
pile_name	
    cards	
        0	
            value	"ACE"
            suite	"CLUBS"
            shortCode	"AC"
            imageUrl	"http://localhost:8080/images/AC.svg"
    status	"success"
</pre>
URL_ALL_PILES = BASE_URL + "/%s/pile/"; Finally, you can list out all the piles there are:

* http://localhost:8080/api/deck/<deckId>/pile/

<pre>
piles	
    0	"remainingCards"
    1	"pile_name"
    2	"discardedCards"
deckId	"59263225-2cd8-46eb-856f-6b31c92bd9c2"
</pre>