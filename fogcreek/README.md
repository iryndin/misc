Fogcreek
========

This is a solution of FogCreek test task for sowtware developers. See [task description](http://www.fogcreek.com/Jobs/Dev/).

Description
-----------

Find a 9 letter string of characters that contains only letters from

`acdegilmnoprstuw`

such that the hash(the_string) is

`910897038977002`

if hash is defined by the following pseudo-code:

```
    Int64 hash (String s) {
        Int64 h = 7
        String letters = "acdegilmnoprstuw"
        for(Int32 i = 0; i < s.length; i++) {
            h = (h * 37 + letters.indexOf(s[i]))
        }
        return h
    }
```

For example, if we were trying to find the 7 letter string where hash(the_string) was `680131659347`, the answer would be `leepadg`.

Solution
--------

### BruteForce

See [BruteForce.java](../fogcreek/BruteForce.java)

It is straightforward. 8 nested loops, in the last hash is checked.

### Reverse Hash

See [ReverseHash.java](../fogcreek/ReverseHash.java)

If you look precisely on hash function, you will see it has some nice features:

* it is long (64-bit) number. Since only 9 characters length string is sought, no overflow is expected
* it is straightforward: no division remainders, bit shifts. Only two simple operation: multiplication and addition.

All this allow us to revert it pretty easy.

We notice, that each character is division remainder by 37. If division remainder is greater than 15, then wrong hash is given (since we have only 15 allowed chars: `acdegilmnoprstuw`).
Then we divide by 37 and go to next char. That's it. 

