for $tuple in join(for $b in doc("samples/xml/books.xml")/book,
                       $tb in $b/title
                   return <tuple>{<b> {$b} </b>, <tb> {$tb} </tb>}</tuple>,

                   for $a in doc("samples/xml/reviews.xml")/entry,
                       $ta in $a/title
                   return <tuple>{ <a> {$a} </a>, <ta> {$ta} </ta> }</tuple>,

                   [tb], [ta]
                  )
return
    <book-with-prices>
        { $tuple,
            <price-review>{ $tuple/a/price }</price-review>,
            <price>{ $tuple/b/price }</price>
        }
    </book-with-prices>