for $b  in doc("input")/book,
    $a  in doc("input")/entry,
    $tb in $b/title,
    $ta in $a/title
where $tb eq $ta
return
    <book-with-prices>
        { $tb,
          <price-review>{ $a/price/text() }</price-review>,
          <price>{ $b/price/text() }</price>
        }
    </book-with-prices>