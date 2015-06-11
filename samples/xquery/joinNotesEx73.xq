for $b1   in doc("input")/book,
    $aj   in $b1/author/first,
    $a1   in $b1/author,
    $af1  in $a1/first,
    $al1  in $a1/last,
    $b2   in doc("input")/book,
    $a21  in $b2/author,
    $af21 in $a21/first,
    $al21 in $a21/last,
    $a22  in $b2/author,
    $af22 in $a22/first,
    $al22 in $a22/last,
    $b3   in doc("input")/book,
    $a3   in $b3/author,
    $af3  in $a3/first,
    $al3  in $a3/last
where $aj eq "John" and
      $af1       eq $af21  and
      $al1       eq $al21  and
      $af22      eq $af3   and
      $al22      eq $al3
return <triplet> { $b1, $b2, $b3 } </triplet>