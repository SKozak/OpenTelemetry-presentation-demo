import * as faker from 'faker/locale/en_US';

const cardTypes = ["Visa", "Mastercard", "American Express"];
const documentTypes = ["ID_CARD", "PASSPORT", "DRIVING_LICENSE"];
export const generateRequest = () => ({
    client : {
        name: faker.name.findName(),
        email: faker.internet.email(),
        pesel: faker.datatype.number({ min: 80111111111, max: 99999999999 }).toString(),
        birthDate: faker.date.future(40, new Date(1985, 0, 1)).toISOString().split('T')[0],
        address: {
            street: faker.address.streetAddress(),
            city: faker.address.city(),
            country: faker.address.country(),
            postalCode: faker.address.zipCode()
        },
        income: faker.datatype.number({ min: 1000, max: 15000 }),
        document: {
            type: faker.random.arrayElement(documentTypes),
            number: faker.random.alphaNumeric(8).toUpperCase(),
            expiry: faker.date.future(3, new Date(2021, 0, 1)).toISOString().split('T')[0]
        },
        employment: {
            employerName: faker.company.companyName(),
            jobTitle: faker.name.jobTitle(),
            startDate: faker.date.future(10).toISOString().split('T')[0]
        }
    },
    cardType: faker.random.arrayElement(cardTypes),
    requestedCardLimit: faker.datatype.number({ min: 5000, max: 20000 }),
});