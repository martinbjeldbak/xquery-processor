for $b in doc("input")/book,
    $a in doc("input")/entry,
    $tb in $b/title,
    $ta in $a/title
where $tb eq $ta
return
    <book-with-prices>
        { $tb,
        <price-review>{ $a/price }</price-review>,
        <price>{ $b/price }</price>
        }
    </book-with-prices>