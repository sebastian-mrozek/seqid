<script lang="ts">
  import type { NumericSequenceDefinition } from "../model";
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  let namespace = "";
  let name = "";
  let start = 1;

  function onClick() {
    dispatch("create", { name, namespace, start });
    namespace = "";
    name = "";
    start = 1;
    console.log(start);
  }

  $: valid = namespace.length > 0 && name.length > 0 && start !== undefined && start !== null;
</script>

<div class="input-group fluid">
  <label for="name">Name</label>
  <input id="name" type="text" bind:value={name} />
  <label for="namespace">Namespace</label>
  <input id="namespace" type="text" bind:value={namespace} />
  <label for="start">Start</label>
  <input id="start" type="number" bind:value={start} />
  <button class="add" on:click={onClick} disabled={!valid}>Add</button>
</div>

<style>
</style>
