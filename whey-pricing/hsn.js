const playwright = require('playwright');
const fs = require('fs');

const products = [
  { url: 'https://www.hsnstore.com/marcas/sport-series/evobeef-hidrolizado-de-carne-2-0-leucina-extra', positionViaText: '2Kg' },
  { url: 'https://www.hsnstore.com/marcas/essential-series/proteina-de-guisante-aislada-2-0', positionViaText: '2Kg'  },
  { url: 'https://www.hsnstore.com/marcas/sport-series/evowhey-protein-2-0', positionViaText: '2Kg'  },
  { url: 'https://www.hsnstore.com/marcas/essential-series/proteina-de-semilla-de-calabaza', positionViaText: '2Kg'  },
  { url: 'https://www.hsnstore.com/marcas/essential-series/proteina-de-arroz-integral-concentrada', positionViaText: '2Kg' },
  { url: 'https://www.hsnstore.com/marcas/food-series/harina-de-arroz-hidrolizada', positionViaText: '3Kg'  },
  { url: 'https://www.hsnstore.com/marcas/raw-series/creatina-excell-100-creapure-en-polvo', positionViaText: '1Kg'  },
  { url: 'https://www.hsnstore.com/marcas/raw-series/creatina-monohidrato-en-polvo', positionViaText: '1Kg' },
  { url: 'https://www.hsnstore.com/marcas/essential-series/digezyme-200mg', positionViaText: '240' },
  { url: 'https://www.hsnstore.com/marcas/raw-series/aminoacidos-esenciales-eaa-s-en-polvo', positionViaText: '1Kg' },
  { url: 'https://www.hsnstore.com/marcas/raw-series/colageno-hidrolizado-bovino', positionViaText: '1Kg' },
  { url: 'https://www.hsnstore.com/marcas/raw-series/l-glutamina-en-polvo', positionViaText: '1Kg' },
];
const delay = ms => new Promise(resolve => setTimeout(resolve, ms));
const prices = {};
let browser = null;

async function scrapUrl(productUrl, { position, positionViaText }) {
  if (!browser) {
    console.log("launching browser");
    browser = await playwright.chromium.launch({
      headless: false,
    });
  }
  try {
    const page = await browser.newPage();
    await page.goto(productUrl);
    await page.waitForTimeout(5000);
    if (positionViaText) {
      await page.locator(`.peso-tamaño-product>label>>text=${positionViaText}`).click();
    } else if (position) {
      await page.locator(`.peso-tamaño-product>label>>nth=${position}`).click();
    } else {
      await page.locator(".peso-tamaño-product>label:not(.no_stock)>>nth=-1").click();
    }
    // await page.screenshot({ path:`/home/davidpoza/prozis-scraper/screenshot-${productUrl}.png` });
    prices[productUrl] = await page.$$eval(".final-price", (element) => {
      return element[0].innerText;
    });
    console.log(productUrl, prices[productUrl]);
  } catch (err) {
    console.log(err);
    await browser.close();
    browser = null;
  }
}

async function main() {
  for (const product of products) {
    await scrapUrl(product.url, { position: product.position, positionViaText: product.positionViaText });
  };
  if (browser) await browser.close();
  let data = JSON.stringify({
      prices,
    date: new Date().getTime()
    });
    fs.writeFileSync(`/home/davidpoza/docker/filesnginx/www/whey-scraper/hsn.json`, data);
}

main();
