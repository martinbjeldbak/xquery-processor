for $tuple in join(for $b in doc("input")/book,
                       $tb in $b/title
                   return <tuple>{<b> {$b} </b>, <tb> {$tb} </tb>}</tuple>,

                   for $a in doc("input")/entry,
                       $ta in $a/title
                   return <tuple>{ <a> {$a} </a>, <ta> {$ta} </ta> }</tuple>,

                   [tb], [ta]
                  )
return
    <book-with-prices>
        { $tuple/tb,
          <price-review>{ $tuple/a/price/text() }</price-review>,
          <price>{ $tuple/b/price/text() }</price>
        }
    </book-with-prices>