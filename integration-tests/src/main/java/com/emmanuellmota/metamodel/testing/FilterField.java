package com.emmanuellmota.metamodel.testing;

import java.math.BigDecimal;

public class FilterField<T> {
   /* private String operator;
   private String value;

   public Predicate generateCriteria(CriteriaBuilder builder, Path field) {
      try {
         int v = Integer.parseInt(value);
         switch (operator) {
         case "lt": return builder.lt(field, v);
         case "le": return builder.le(field, v);
         case "gt": return builder.gt(field, v);
         case "ge": return builder.ge(field, v);
         case "eq": return builder.equal(field, v);
         }
      } catch (NumberFormatException e) {
         switch (operator) {
         case "endsWith": return builder.like(field, "%" + value);
         case "startsWith": return builder.like(field, value + "%");
         case "contains": return builder.like(field, "%" + value + "%");
         case "eq": return builder.equal(field, value);
         }
      }

      return null;
   } */

   // Integer limit; // limit(10); // limit to at most 10 results
   // Integer skip; // skip(10); // skip the first 10 results

   // String ascending; // ascending("score");
   // String descending; // descending("score");

   BigDecimal lessThan; // lessThan("wins", 50);
   BigDecimal lessThanOrEqualTo; // lessThanOrEqualTo("wins", 50);
   BigDecimal greaterThan; // greaterThan("wins", 50);
   BigDecimal greaterThanOrEqualTo; // greaterThanOrEqualTo("wins", 50);

   MinMaxField between;
   MinMaxField beside;

   T[] containedIn; // containedIn("playerName", ["Jonathan Walsh", "Dario Wunsch", "Shawn Simon"]);
   T[] notContainedIn; // notContainedIn("playerName", ["Jonathan Walsh", "Dario Wunsch", "Shawn Simon"]);

   // T exists; // exists("score");
   // T doesNotExist; // doesNotExist("score");

   // String includeKey; // includeKey("post");

   // String matchesKeyInQuery; // matchesKeyInQuery("hometown", "city", $teamQuery);
   // String matchesQuery; // matchesQuery("post", $innerQuery);
   // String doesNotMatchQuery; // doesNotMatchQuery("post", $innerQuery);

   /* $pipeline = [
        'group' => [
            'objectId' => null,
            'total' => [ '$sum' => '$score']
        ]
    ];
    $pipeline = [
        'project' => [
            'name' => 1
        ]
    ];
    $pipeline = [
        'match' => [
            'score' => [ '$gt' => 15 ]
        ]
    ]; */
   // String aggregate; // aggregate($pipeline);

   // String distinct; // distinct('score');

   T equalTo; // equalTo("arrayKey", 2);
   // T[] containsAll; // containsAll("arrayKey", [2, 3, 4]);

   String startsWith; // startsWith("name", "Big Daddy's");
   String endsWith; // endsWith("name", "Sauce");
}
