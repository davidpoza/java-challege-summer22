const playwright = require('playwright');
const fs = require('fs');

const products = [
	'https://www.hsnstore.com/marcas/sport-series/evobeef-hidrolizado-de-carne-2-0-leucina-extra',
	'https://www.hsnstore.com/marcas/essential-series/proteina-de-guisante-aislada-2-0',
	'https://www.hsnstore.com/marcas/raw-series/native-whey-concentrate',
	'https://www.hsnstore.com/marcas/essential-series/proteina-de-semilla-de-calabaza',
	'https://www.hsnstore.com/marcas/essential-series/proteina-de-arroz-integral-concentrada',
	'https://www.hsnstore.com/marcas/food-series/harina-de-arroz-hidrolizada',
	'https://www.hsnstore.com/marcas/raw-series/creatina-excell-100-creapure-en-polvo'
];
const prices = {};

let browser;
const delay = ms => new Promise(resolve => setTimeout(resolve, ms));

async function scrapUrl(productUrl) {
  try {
	  console.log("launching browser");
	  browser = await playwright.chromium.launch({
				headless: false,
	  });
	  const page = await browser.newPage();
	  await page.goto(productUrl);
	  await page.waitForTimeout(4000);	
	  await page.locator('.peso-tamaño-product>label>>nth=-1').click()
	  // await page.screenshot({ path:`/home/davidpoza/prozis-scraper/screenshot-${productUrl}.png` });
	  prices[productUrl] = await page.$$eval('.final-price', (element) => {
		return element[0].innerText;
	  });
	  console.log(prices[productUrl]);
	  await browser.close();	      
  } catch (err) {
	console.log(err)
    await browser.close();
  }
}

async function main() {
	for (const productUrl of products) {
		await scrapUrl(productUrl);
	};
	let data = JSON.stringify({
	    prices,
		date: new Date().getTime()
	  });
    fs.writeFileSync(`/home/davidpoza/docker/filesnginx/www/whey-scraper/hsn.json`, data);
}

main();
