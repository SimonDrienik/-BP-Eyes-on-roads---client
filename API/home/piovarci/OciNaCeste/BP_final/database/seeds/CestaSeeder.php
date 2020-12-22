<?php

use App\Models\Cesta;
use Illuminate\Database\Seeder;

class CestaSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $cesty = [
            [
                'nazov' => 'undefined',
                'kraj_id' => 1,
                'katastralne_uzemie_id' => 1,
                'obec_id' => 1,
                'spravca_id' => 1,

            ],
            [
                'nazov' => 'undefined',
                'kraj_id' => 2,
                'katastralne_uzemie_id' => 2,
                'obec_id' => 2,
                'spravca_id' => 2,
            ],
        ];

        foreach ($cesty as $cesta) {
            Cesta::create(array(
                'nazov' => $cesta['nazov'],
                'kraj_id' => $cesta['kraj_id'],
                'katastralne_uzemie_id' => $cesta['katastralne_uzemie_id'],
                'obec_id' => $cesta['obec_id'],
                'spravca_id' => $cesta['spravca_id'],
            ));
        }
    }
}
