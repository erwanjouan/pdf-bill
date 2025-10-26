# pdf-bill

(French) Pdf invoice generator for consulting fees, based on

- Java 17
- Maven
- [Itext 9](https://itextpdf.com/)

With:

- [customizable input](./input/billing.config.json)
- [optional Aws Lambda Cdk deployment](./infra/lib/pdf-bill-stack.ts)

[Output example is here](./output/Facture_MaStructure_MonPorteurd'affaire_202505.pdf)

See [Makefile](./Makefile)

## Github actions

https://dev.to/koseimori/a-practical-guide-to-continuous-delivery-with-github-actions-and-aws-cdk-435k